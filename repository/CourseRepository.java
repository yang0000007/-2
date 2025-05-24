package com.seniorlearn.onlinelearning.repository;

import com.seniorlearn.onlinelearning.model.Course;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long>, JpaSpecificationExecutor<Course> {

    @Query("SELECT c FROM Course c LEFT JOIN c.tags t WHERE " +
            "(COALESCE(:tags) IS NULL OR t.name IN :tags)")
    Page<Course> searchCourses(@Param("keyword") String keyword,
                               @Param("categoryId") Long categoryId,
                               @Param("tags") List<String> tags,
                               Pageable pageable);
}