package com.seniorlearn.onlinelearning.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record CourseDTO(
        Long id,
        @NotBlank @Size(max = 100) String title,
        String description,
        String categoryName,
        Set<String> tags, // 确保类型与实体类一致
        Integer durationMinutes,
        String coverImageUrl,
        Integer studentCount // 修改为Integer类型
) {}
