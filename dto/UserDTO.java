package com.seniorlearn.onlinelearning.dto;

import com.seniorlearn.onlinelearning.model.Role;
import jakarta.validation.constraints.*;

public record UserDTO(
        @NotBlank(message = "用户名不能为空")
        @Size(min = 3, max = 20, message = "用户名长度3-20字符")
        String username,

        @NotBlank(message = "密码不能为空")
        @Size(min = 6, max = 20, message = "密码长度6-20字符")
        String password,

        @Email(message = "邮箱格式不正确")
        String email,

        @NotNull(message = "角色不能为空")
        Role role
) {}