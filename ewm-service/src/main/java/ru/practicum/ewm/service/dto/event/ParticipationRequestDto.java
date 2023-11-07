package ru.practicum.ewm.service.dto.event;

import lombok.Builder;
import lombok.Data;
import ru.practicum.ewm.service.model.Status;

import java.time.LocalDateTime;

@Data
@Builder
public class ParticipationRequestDto {

    private LocalDateTime created;/* дата и время создания заявки */
    private Integer event;/* идентификатор события */
    private Integer id;/* идентификатор заявки */
    private Integer requester;/* идентификатор пользователя отправившего заявку */
    private Status status;/* статус заявки */
}
