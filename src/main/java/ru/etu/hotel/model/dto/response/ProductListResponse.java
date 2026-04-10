package ru.etu.hotel.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductListResponse {
    private Long total;
    private Integer limit;
    private Integer offset;
    private List<ProductResponse> items;
}
