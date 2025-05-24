package com.seniorlearn.onlinelearning.controller;

import com.seniorlearn.onlinelearning.dto.*;
import com.seniorlearn.onlinelearning.model.Course;
import com.seniorlearn.onlinelearning.service.CourseService;
import com.seniorlearn.onlinelearning.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import com.seniorlearn.onlinelearning.model.Tag;


@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Transactional
public class AdminUserController {

    private final UserService userService;
    private final CourseService courseService;

    @GetMapping
    public Page<UserResponseDTO> getUsers(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String role,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return userService.searchUsers(username, email, role, pageable);
    }

    @PutMapping("/{userId}/role")
    public ResponseEntity<Void> updateUserRole(
            @PathVariable Long userId,
            @RequestBody RoleUpdateDTO roleDTO
    ) {
        userService.updateUserRole(userId, roleDTO.role());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/courses/{courseId}/approval")
    public ResponseEntity<CourseDTO> approveCourse(
            @PathVariable Long courseId,
            @RequestBody CourseApprovalDTO approvalDTO
    ) {
        Course course = courseService.approveCourse(courseId, approvalDTO);
        return ResponseEntity.ok(convertToDTO(course));
    }


    @PostMapping("/courses/batch-update")
    public ResponseEntity<Void> batchUpdateCourses(
            @RequestBody List<CourseBatchUpdateDTO> updates
    ) {
        courseService.batchUpdate(updates);
        return ResponseEntity.noContent().build();
    }


    @PatchMapping("/{userId}/status")
    public ResponseEntity<Void> updateUserStatus(
            @PathVariable Long userId,
            @RequestBody UserStatusUpdateDTO statusDTO
    ) {
        userService.updateUserStatus(userId, statusDTO.enabled());
        return ResponseEntity.noContent().build();
    }

    private CourseDTO convertToDTO(Course course) {
        return new CourseDTO(
                course.getId(),
                course.getTitle(),
                course.getDescription(),
                course.getCategory().getName(),
                course.getTags().stream()
                        .map(Tag::getName) // 必须确认Tag类路径正确
                        .collect(Collectors.toSet()),
                course.getDurationMinutes(),
                course.getCoverImageUrl(),
                course.getStudentCount()
        );
    }

}