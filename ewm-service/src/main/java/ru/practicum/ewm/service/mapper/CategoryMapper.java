package ru.practicum.ewm.service.mapper;

import ru.practicum.ewm.service.dto.category.CategoryDto;
import ru.practicum.ewm.service.dto.category.NewCategoryDto;
import ru.practicum.ewm.service.model.Category;

import java.util.List;
import java.util.stream.Collectors;

public class CategoryMapper {

    public static Category map(NewCategoryDto newCat) {
        return Category.builder()
                .name(newCat.getName())
                .build();
    }

    public static CategoryDto map(Category cat) {
        return CategoryDto.builder()
                .id(cat.getId())
                .name(cat.getName())
                .build();
    }

    public static List<CategoryDto> map(List<Category> categories) {
        return categories.stream()
                .map(CategoryMapper::map)
                .collect(Collectors.toList());
    }

    public static Category map(CategoryDto updateCat) {
        return Category.builder()
                .id(updateCat.getId())
                .name(updateCat.getName())
                .build();
    }
}
