package com.seniorlearn.onlinelearning.service;

import com.seniorlearn.onlinelearning.dto.CourseApprovalDTO;
import com.seniorlearn.onlinelearning.dto.CourseBatchUpdateDTO;
import com.seniorlearn.onlinelearning.dto.CourseDTO;
import com.seniorlearn.onlinelearning.dto.CourseSearchDTO;
import com.seniorlearn.onlinelearning.model.Course;
import com.seniorlearn.onlinelearning.model.CourseCategory;
import com.seniorlearn.onlinelearning.repository.CourseCategoryRepository;
import com.seniorlearn.onlinelearning.repository.CourseRepository;
import com.seniorlearn.onlinelearning.specification.CourseSpecifications;
import jakarta.persistence.Cacheable;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.seniorlearn.onlinelearning.model.Tag;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;
    private final CourseCategoryRepository courseCategoryRepository;


    public Page<CourseDTO> searchCourses(CourseSearchDTO searchDTO) {
        int page = Optional.ofNullable(searchDTO.page()).orElse(0);
        int size = Optional.ofNullable(searchDTO.size()).orElse(10);

        Sort sort = parseSort(searchDTO.sortBy());

        Pageable pageable = PageRequest.of(page, size, sort);

        log.info("Executing database query for: {}", searchDTO);
        Specification<Course> spec = Specification.where(
                        CourseSpecifications.withKeyword(searchDTO.keyword()))
                .and(CourseSpecifications.withCategory(searchDTO.categoryId()))
                .and(CourseSpecifications.withTags(searchDTO.tags()))
                .and(CourseSpecifications.withDurationBetween(
                        searchDTO.minDuration(), searchDTO.maxDuration()));


        return courseRepository.findAll(spec, pageable)
                .map(this::convertToDTO);
    }

    private Sort parseSort(String sortBy) {
        return switch (sortBy) {
            case "latest" -> Sort.by(Sort.Direction.DESC, "createdAt");
            case "popular" -> Sort.by(Sort.Direction.DESC, "studentsCount");
            case "duration" -> Sort.by(Sort.Direction.DESC, "durationMinutes");
            default -> Sort.by(Sort.Direction.ASC, "title");
        };
    }

    public String getVideoPath(Long courseId) {
        return courseRepository.findById(courseId)
                .map(Course::getVideoUrl)
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));
    }


    private CourseDTO convertToDTO(Course course) {
        Set<String> tagNames = course.getTags().stream()
                .map(Tag::getName) // 假设Tag实体有getName方法
                .collect(Collectors.toSet());

        return new CourseDTO(
                course.getId(),
                course.getTitle(),
                course.getDescription(),
                course.getCategory().getName(),
                tagNames, // 使用标签名称集合
                course.getDurationMinutes(),
                course.getCoverImageUrl(),
                course.getStudents().size()
        );
    }

    @Transactional
    public Course approveCourse(Long courseId, CourseApprovalDTO approvalDTO) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));

        return courseRepository.save(course);
    }

    @Transactional
    public void batchUpdate(List<CourseBatchUpdateDTO> updates) {
        updates.forEach(update -> {
            Course course = courseRepository.findById(update.courseId())
                    .orElseThrow(() -> new EntityNotFoundException("Course not found"));

            // 根据分类名称查找
            CourseCategory category = courseCategoryRepository.findByName(update.category())
                    .orElseThrow(() -> new IllegalArgumentException("无效分类"));

            course.setCategory(category);
            courseRepository.save(course);
        });
    }

}
