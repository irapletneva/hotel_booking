package ru.etu.hotel.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "classification_element")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClassificationElement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "class_code", length = 64, nullable = false, unique = true)
    private String classCode;

    @Column(name = "name", length = 256, nullable = false)
    private String name;

    @Column(name = "is_terminal", nullable = false)
    private Boolean isTerminal;

    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder;

    @Column(name = "unit_of_measure", length = 64)
    private String unitOfMeasure;

    @Column(name = "parent_id")
    private Integer parentId;

    @Transient
    private Integer level;
}
