package ru.practicum.ewm.service.dto.event;

import lombok.Data;

@Data
public class NewEventDto {

    private String annotation; //краткое описание
    private Integer category; //id категории события
    private String description; //полное описание
    private String eventDate; //дата события
    private Location location; //широта и долгота места
    private Boolean paid; //(бес)платный вход
    private Integer participantLimit; //ограничение на кол-во участников
    private Boolean requestModeration; //пре-модерация заявок на участие
    private String title; //заголовок
}
