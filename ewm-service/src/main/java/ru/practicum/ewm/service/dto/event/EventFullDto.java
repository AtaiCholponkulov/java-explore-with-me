package ru.practicum.ewm.service.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.ewm.service.dto.category.CategoryDto;
import ru.practicum.ewm.service.dto.user.UserShortDto;
import ru.practicum.ewm.service.model.State;

import java.time.LocalDateTime;

import static ru.practicum.ewm.service.valid.Validator.DATE_TIME_PATTERN;

@Getter
@Setter
public class EventFullDto extends EventShortDto {

    /*
    createdOn - дата и время создания события
    description - полное описание
    location - широта и долгота места
    participantLimit - ограничение на кол-во участников
    publishedOn - дата и время публикации события
    requestModeration - пре-модерация заявок на участие
    state - состояние жизненного цикла
     */
    @JsonFormat(pattern = DATE_TIME_PATTERN)
    private LocalDateTime createdOn;
    private String description;
    private LocationDto location;
    private Integer participantLimit;
    @JsonFormat(pattern = DATE_TIME_PATTERN)
    private LocalDateTime publishedOn;
    private Boolean requestModeration;
    private State state;

    public EventFullDto(String annotation,
                        CategoryDto category,
                        Integer confirmedRequests,
                        LocalDateTime eventDate,
                        Integer id,
                        UserShortDto initiator,
                        Boolean paid,
                        String title,
                        int views,
                        LocalDateTime createdOn,
                        String description,
                        LocationDto location,
                        Integer participantLimit,
                        LocalDateTime publishedOn,
                        Boolean requestModeration,
                        State state) {
        super(annotation, category, confirmedRequests, eventDate, id, initiator, paid, title, views);
        this.createdOn = createdOn;
        this.description = description;
        this.location = location;
        this.participantLimit = participantLimit;
        this.publishedOn = publishedOn;
        this.requestModeration = requestModeration;
        this.state = state;
    }
}
