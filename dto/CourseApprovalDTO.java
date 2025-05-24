package com.seniorlearn.onlinelearning.dto;

import jakarta.validation.constraints.Size;

public record CourseApprovalDTO(
        boolean approved,
        @Size(max = 200) String reviewComment
) {}
