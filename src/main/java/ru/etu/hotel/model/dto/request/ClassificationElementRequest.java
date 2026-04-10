package ru.etu.hotel.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClassificationElementRequest {
    private String classCode;
    private String name;
    private Boolean isTerminal;
    private Integer sortOrder;
    private String unitOfMeasure;
    private Integer parentId;
}
