package com.seniorlearn.onlinelearning.specification;

import com.seniorlearn.onlinelearning.model.Course;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class CourseSpecifications {

    public static Specification<Course> withKeyword(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.isEmpty()) return null;
            String pattern = "%" + keyword.toLowerCase() + "%";
            return cb.or(
                    cb.like(cb.lower(root.get("title")), pattern),
                    cb.like(cb.lower(root.get("description")), pattern)
            );
        };
    }

    public static Specification<Course> withCategory(Long categoryId) {
        return (root, query, cb) ->
                categoryId != null ? cb.equal(root.get("category").get("id"), categoryId) : null;
    }

    public static Specification<Course> withTags(List<String> tags) {
        return (root, query, cb) -> {
            if (tags == null || tags.isEmpty()) return null;
            return cb.isMember(tags, root.get("tags"));
        };
    }

    public static Specification<Course> withDurationBetween(Integer min, Integer max) {
        return (root, query, cb) -> {
            if (min == null && max == null) return null;
            List<jakarta.persistence.criteria.Predicate> predicates = new ArrayList<>(); // 使用正确的Predicate类型
            if (min != null) predicates.add(cb.greaterThanOrEqualTo(root.get("durationMinutes"), min));
            if (max != null) predicates.add(cb.lessThanOrEqualTo(root.get("durationMinutes"), max));
            return cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        };
    }
}