package ru.practicum.ewm.service.dto.compilation;

import lombok.Builder;
import lombok.Data;
import ru.practicum.ewm.service.dto.event.EventShortDto;

import java.util.List;

@Data
@Builder
public class CompilationDto {

    private List<EventShortDto> events;
    private Integer id;
    private Boolean pinned;
    private String title;
}
