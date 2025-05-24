package com.seniorlearn.onlinelearning.dto;

import com.seniorlearn.onlinelearning.model.Role;

public record UserResponseDTO(
        Long id,
        String username,
        String email,
        Role role
) {}