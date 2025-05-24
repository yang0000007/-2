package com.seniorlearn.onlinelearning.dto;

import java.util.List;

public record CourseSearchDTO(
        String keyword,
        Long categoryId,
        List<String> tags,
        Integer minDuration,
        Integer maxDuration,
        String sortBy, // latest, popular, duration
        Integer page,
        Integer size
) {}