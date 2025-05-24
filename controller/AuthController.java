package com.seniorlearn.onlinelearning.controller;

import com.seniorlearn.onlinelearning.dto.UserDTO;
import com.seniorlearn.onlinelearning.model.User;
import com.seniorlearn.onlinelearning.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

///​**​
//        * 认证相关API控制器
// */
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        // 简单模拟验证
        if ("123".equals(request.getUsername()) && "123".equals(request.getPassword())) {
            String token = Jwts.builder()
                    .setSubject(request.getUsername())
                    .signWith(SignatureAlgorithm.HS512, "secret_key")
                    .compact();
            return ResponseEntity.ok().body(Map.of("token", token));
        }
        return ResponseEntity.status(401).build();
    }

@PostMapping("/register")
public ResponseEntity<String> registerUser(@RequestBody UserDTO userDTO) { // 改为接收DTO
    userService.createUser(userDTO); // 确保UserService有对应方法
    return ResponseEntity.ok("用户注册成功！");
}
}