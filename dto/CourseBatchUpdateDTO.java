package com.seniorlearn.onlinelearning.dto;

public record CourseBatchUpdateDTO(
        Long courseId,
        boolean visible,
        String category
) {}