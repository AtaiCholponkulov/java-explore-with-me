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

    protected String annotation;/* краткое описание */
    protected CategoryDto category;/* id категории события */
    protected Integer confirmedRequests;/* одобренные заявки на участие */
    @JsonFormat(pattern = DATE_TIME_PATTERN)
    protected LocalDateTime eventDate;/* дата события */
    protected Integer id;/* идент-ор события */
    protected UserShortDto initiator;/* автор события */
    protected Boolean paid;/* (бес)платный вход */
    protected String title;/* заголовок */
    protected int views;/* кол-во просмотров */

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
