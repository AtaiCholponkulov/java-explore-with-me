package ru.practicum.ewm.service.service;

import org.springframework.stereotype.Service;
import ru.practicum.ewm.service.controller.EventSort;
import ru.practicum.ewm.service.dto.compilation.CompilationDto;
import ru.practicum.ewm.service.dto.event.EventFullDto;
import ru.practicum.ewm.service.dto.event.EventShortDto;
import ru.practicum.ewm.service.model.Category;

import java.time.LocalDateTime;
import java.util.List;

@Service
public interface PublicService {

    //------------------------------------------------Public: Категории-------------------------------------------------
    List<Category> getCategories(int from, int size);

    Category getCategory(int catId);

    //-------------------------------------------------Public: События--------------------------------------------------
    List<EventShortDto> getEvents(String text,
                                  List<Integer> categories,
                                  Boolean paid,
                                  LocalDateTime rangeStart,
                                  LocalDateTime rangeEnd,
                                  boolean onlyAvailable,
                                  EventSort sort,
                                  int from,
                                  int size);

    EventFullDto getEvent(int id);

    //----------------------------------------------Public: Подборки событий--------------------------------------------
    List<CompilationDto> getCompilations(Boolean pinned, int from, int size);

    CompilationDto getCompilation(int compId);
}
