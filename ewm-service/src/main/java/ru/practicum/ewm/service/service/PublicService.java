package ru.practicum.ewm.service.service;

import ru.practicum.ewm.service.controller.EventSort;
import ru.practicum.ewm.service.dto.category.CategoryDto;
import ru.practicum.ewm.service.dto.comment.CommentDto;
import ru.practicum.ewm.service.dto.compilation.CompilationDto;
import ru.practicum.ewm.service.dto.event.EventFullDto;
import ru.practicum.ewm.service.dto.event.EventShortDto;

import java.time.LocalDateTime;
import java.util.List;

public interface PublicService {

    /** Public: Категории */
    List<CategoryDto> getCategories(int from, int size);

    CategoryDto getCategory(int catId);

    /** Public: События */
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

    /** Public: Подборки событий */
    List<CompilationDto> getCompilations(Boolean pinned, int from, int size);

    CompilationDto getCompilation(int compId);

    /** Public: Комментарии */
    List<CommentDto> getEventComments(int eventId);
}
