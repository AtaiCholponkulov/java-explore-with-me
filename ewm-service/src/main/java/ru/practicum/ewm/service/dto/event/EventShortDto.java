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
public class EventShortDto {

    private String annotation; //краткое описание
    private CategoryDto category; //id категории события
    private Integer confirmedRequests; //одобренные заявки на участие
    @JsonFormat(pattern = DATE_TIME_PATTERN)
    private LocalDateTime eventDate; //дата события
    private Integer id; //идент-ор события
    private UserShortDto initiator; //автор события
    private Boolean paid; //(бес)платный вход
    private String title; //заголовок
    private int views; //кол-во просмотров
}
