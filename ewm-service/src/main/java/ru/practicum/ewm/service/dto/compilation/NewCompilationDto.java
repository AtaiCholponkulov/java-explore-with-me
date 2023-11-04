package ru.practicum.ewm.service.dto.compilation;

import lombok.Data;

import java.util.List;

@Data
public class NewCompilationDto {

    private List<Integer> events;//Список идентификаторов событий входящих в подборку
    private Boolean pinned;//Закреплена ли подборка на главной странице сайта
    private String title;//Заголовок подборки
}
