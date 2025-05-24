package com.seniorlearn.onlinelearning.service;

import org.springframework.core.io.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import org.springframework.http.HttpHeaders;

@Service
@RequiredArgsConstructor
public class VideoStreamService {
    private final ResourceLoader resourceLoader;

    public ResponseEntity<Resource> streamVideo(String videoPath, HttpHeaders headers) {
        Resource videoResource = resourceLoader.getResource("file:" + videoPath);

        long contentLength;
        try {
            contentLength = videoResource.contentLength();
        } catch (IOException e) {
            throw new RuntimeException("视频文件读取失败", e);
        }

        String rangeHeader = headers.getFirst(HttpHeaders.RANGE); // 使用 Spring 的 HttpHeaders.RANGE
        if (rangeHeader == null) {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(contentLength))
                    .body(videoResource);
        }

        String[] ranges = rangeHeader.substring("bytes=".length()).split("-");
        long start = Long.parseLong(ranges[0]);
        long end = ranges.length > 1 ? Long.parseLong(ranges[1]) : contentLength - 1;

        if (end >= contentLength) end = contentLength - 1;

        long finalEnd = end;
        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                .header(HttpHeaders.CONTENT_TYPE, "video/mp4")
                .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(finalEnd - start + 1))
                .header(HttpHeaders.CONTENT_RANGE, "bytes " + start + "-" + finalEnd + "/" + contentLength)
                .body(new InputStreamResource(() -> {
                    try {
                        InputStream inputStream = videoResource.getInputStream();
                        inputStream.skip(start);
                        return new LimitedInputStream(inputStream, finalEnd - start + 1);
                    } catch (IOException e) {
                        throw new RuntimeException("视频流传输失败", e);
                    }
                }));
    }

    private static class LimitedInputStream extends InputStream {
        private final InputStream wrapped;
        private long remaining;

        public LimitedInputStream(InputStream in, long limit) {
            this.wrapped = in;
            this.remaining = limit;
        }

        @Override
        public int read() throws IOException {
            if (remaining <= 0) return -1;
            int result = wrapped.read();
            if (result != -1) remaining--;
            return result;
        }

        @Override
        public int read(byte[] b, int off, int len) throws IOException {
            if (remaining <= 0) return -1;
            int adjustedLen = (int) Math.min(len, remaining);
            int result = wrapped.read(b, off, adjustedLen);
            if (result != -1) remaining -= result;
            return result;
        }

        @Override
        public void close() throws IOException {
            wrapped.close();
        }
    }
}