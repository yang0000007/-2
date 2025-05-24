package com.seniorlearn.onlinelearning.config;

import com.seniorlearn.onlinelearning.dto.CourseDTO;
import com.seniorlearn.onlinelearning.dto.CourseSearchDTO;
import com.seniorlearn.onlinelearning.service.CourseService;
import com.seniorlearn.onlinelearning.service.VideoStreamService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.HttpHeaders;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;
    private final VideoStreamService videoService;

    @GetMapping("/search")
    public ResponseEntity<Page<CourseDTO>> searchCourses(
            @ModelAttribute CourseSearchDTO searchDTO) {
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(30, TimeUnit.MINUTES))
                .body(courseService.searchCourses(searchDTO));
    }

    @GetMapping("/{courseId}/stream")
    public ResponseEntity<Resource> streamCourseVideo(
            @PathVariable Long courseId,
            @RequestHeader HttpHeaders headers) {
        String videoPath = courseService.getVideoPath(courseId);
        return videoService.streamVideo(videoPath, headers); // 使用实例调用
    }
}