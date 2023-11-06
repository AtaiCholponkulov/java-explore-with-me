package ru.practicum.ewm.service.dto.event;

import lombok.Data;

@Data
public class UpdateEventUserRequestDto {

    /*
    annotation - краткое описание
    category - id категории события
    description - полное описание
    eventDate - дата события
    location - широта и долгота места
    paid - (бес)платный вход
    participantLimit - ограничение на кол-во участников
    requestModeration - пре-модерация заявок на участие
    stateAction - изменение состояния события (SEND_TO_REVIEW, CANCEL_REVIEW)
    title - заголовок
     */
    private String annotation;
    private Integer category;
    private String description;
    private String eventDate;
    private LocationDto location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private PrivateStateAction stateAction;
    private String title;
}
