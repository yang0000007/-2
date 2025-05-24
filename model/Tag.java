package com.seniorlearn.onlinelearning.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter // 必须要有Getter
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 如果不用Lombok需手动添加
    @Getter
    @Column(unique = true)
    private String name; // 确保字段名称正确

}