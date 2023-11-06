package ru.practicum.ewm.service.dto.event;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateEventAdminRequestDto {

    private String annotation;
    private Integer category;
    private String description;
    private String eventDate;
    private LocationDto location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private AdminStateAction stateAction;
    private String title;
}
