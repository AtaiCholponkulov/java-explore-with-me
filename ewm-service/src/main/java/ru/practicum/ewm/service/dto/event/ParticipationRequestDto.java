package ru.practicum.ewm.service.dto.event;

import lombok.Builder;
import lombok.Data;
import ru.practicum.ewm.service.model.Status;

import java.time.LocalDateTime;

@Data
@Builder
public class ParticipationRequestDto {

    /*
    created - дата и время создания заявки
    event - идентификатор события
    id - идентификатор заявки
    requester - идентификатор пользователя отправившего заявку
    status - статус заявки
     */
    private LocalDateTime created;
    private Integer event;
    private Integer id;
    private Integer requester;
    private Status status;
}
