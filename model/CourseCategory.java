package com.seniorlearn.onlinelearning.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "course_categories")
@Data
public class CourseCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    private String iconUrl;
}