package ru.practicum.ewm.service.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import ru.practicum.ewm.service.dto.category.CategoryDto;
import ru.practicum.ewm.service.dto.user.UserShortDto;

import java.time.LocalDateTime;

import static ru.practicum.ewm.service.valid.Validator.DATE_TIME_PATTERN;

@Data
@Builder
public class EventFullDto {

    private String annotation; //краткое описание
    private CategoryDto category; //id категории события
    private int confirmedRequests; //одобренные заявки на участие
    @JsonFormat(pattern = DATE_TIME_PATTERN)
    private LocalDateTime createdOn; //дата и время создания события
    private String description; //полное описание
    @JsonFormat(pattern = DATE_TIME_PATTERN)
    private LocalDateTime eventDate; //дата события
    private Integer id; //идент-ор события
    private UserShortDto initiator; //автор события
    private Location location; //широта и долгота места
    private Boolean paid; //(бес)платный вход
    private Integer participantLimit; //ограничение на кол-во участников
    @JsonFormat(pattern = DATE_TIME_PATTERN)
    private LocalDateTime publishedOn; //дата и время публикации события
    private Boolean requestModeration; //пре-модерация заявок на участие
    private State state; //состояние жизненного цикла
    private String title; //заголовок
    private int views; //кол-во просмотров
}
