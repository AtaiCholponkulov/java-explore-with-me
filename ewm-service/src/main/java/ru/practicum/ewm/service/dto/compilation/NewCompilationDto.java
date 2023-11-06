package ru.practicum.ewm.service.dto.compilation;

import lombok.Data;

import java.util.List;

@Data
public class NewCompilationDto {

    /*
    * events - Список идентификаторов событий входящих в подборку
    * pinned - Закреплена ли подборка на главной странице сайта
    * title - Заголовок подборки
    */
    private List<Integer> events;
    private Boolean pinned;
    private String title;
}
