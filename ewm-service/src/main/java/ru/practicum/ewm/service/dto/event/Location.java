package ru.practicum.ewm.service.dto.event;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Location {

    private Float lat;
    private Float lon;
}
