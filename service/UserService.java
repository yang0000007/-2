package com.seniorlearn.onlinelearning.service;

import com.seniorlearn.onlinelearning.dto.UserDTO;
import com.seniorlearn.onlinelearning.dto.UserResponseDTO;
import com.seniorlearn.onlinelearning.model.User;
import com.seniorlearn.onlinelearning.repository.UserRepository;
import com.seniorlearn.onlinelearning.model.Role;
import com.seniorlearn.onlinelearning.specification.UserSpecifications;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.dao.DataIntegrityViolationException;


@Service
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;


    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(UserDTO userDTO) {
        validateExistingUser(userDTO.username(), userDTO.email());
        String encodedPassword = passwordEncoder.encode(userDTO.password());

        User user = new User(
                userDTO.username(),
                passwordEncoder.encode(userDTO.password()),
                userDTO.email(),
                userDTO.role() != null ? userDTO.role() : Role.USER
        );

        try {
            return userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("用户名或邮箱已存在", e);
        }
    }

    public void updateUserRole(Long userId, String role) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("用户不存在"));
        user.setRole(Role.valueOf(role));
        userRepository.save(user);
    }

    public void updateUserStatus(Long userId, boolean enabled) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("用户不存在"));
        user.setEnabled(enabled);
        userRepository.save(user);
    }

    public Page<UserResponseDTO> searchUsers(String username, String email, String role, Pageable pageable) {
        Specification<User> spec = Specification.where(UserSpecifications.withUsernameLike(username))
                .and(UserSpecifications.withEmailLike(email))
                .and(UserSpecifications.withRole(role));
        return userRepository.findAll(spec, pageable)
                .map(user -> new UserResponseDTO(
                        user.getId(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getRole()
                ));
    }

    private void validateExistingUser(String username, String email) {
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("用户名已存在");
        }
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("邮箱已被注册");
        }
    }
}