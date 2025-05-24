package com.seniorlearn.onlinelearning.repository;

import com.seniorlearn.onlinelearning.model.CourseCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CourseCategoryRepository extends JpaRepository<CourseCategory, Long> {
    Optional<CourseCategory> findByName(String name); // 确保这个方法已定义
}