package ru.practicum.ewm.service.mapper;

import ru.practicum.ewm.service.dto.event.ParticipationRequestDto;
import ru.practicum.ewm.service.model.ParticipationRequest;

import java.util.List;
import java.util.stream.Collectors;

public class ParticipationRequestMapper {

    public static ParticipationRequestDto map(ParticipationRequest request) {
        return ParticipationRequestDto.builder()
                .created(request.getCreated())
                .event(request.getEvent().getId())
                .id(request.getId())
                .requester(request.getRequester().getId())
                .status(request.getStatus())
                .build();
    }

    public static List<ParticipationRequestDto> map(List<ParticipationRequest> requests) {
        return requests.stream()
                .map(ParticipationRequestMapper::map)
                .collect(Collectors.toList());
    }
}
