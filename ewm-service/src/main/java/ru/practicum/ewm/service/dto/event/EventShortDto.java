package ru.practicum.ewm.service.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.ewm.service.dto.category.CategoryDto;
import ru.practicum.ewm.service.dto.user.UserShortDto;

import java.time.LocalDateTime;

import static ru.practicum.ewm.service.valid.Validator.DATE_TIME_PATTERN;

@Getter
@Setter
public class EventShortDto {

    /*
    annotation - краткое описание
    category - id категории события
    confirmedRequests - одобренные заявки на участие
    eventDate - дата события
    id - идент-ор события
    initiator - автор события
    paid - (бес)платный вход
    title - заголовок
    views - кол-во просмотров
     */
    protected String annotation;
    protected CategoryDto category;
    protected Integer confirmedRequests;
    @JsonFormat(pattern = DATE_TIME_PATTERN)
    protected LocalDateTime eventDate;
    protected Integer id;
    protected UserShortDto initiator;
    protected Boolean paid;
    protected String title;
    protected int views;

    public EventShortDto(String annotation,
                         CategoryDto category,
                         Integer confirmedRequests,
                         LocalDateTime eventDate,
                         Integer id,
                         UserShortDto initiator,
                         Boolean paid,
                         String title,
                         int views) {
        this.annotation = annotation;
        this.category = category;
        this.confirmedRequests = confirmedRequests;
        this.eventDate = eventDate;
        this.id = id;
        this.initiator = initiator;
        this.paid = paid;
        this.title = title;
        this.views = views;
    }
}
