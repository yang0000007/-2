package com.seniorlearn.onlinelearning.dto;

import jakarta.validation.constraints.NotBlank;

public record RoleUpdateDTO(@NotBlank String role) {}
