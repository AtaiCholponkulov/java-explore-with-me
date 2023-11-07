package ru.practicum.ewm.service.dto.category;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryDto {

    private Integer id;
    private String name;
}
