package ru.practicum.ewm.service.service;

import org.springframework.stereotype.Service;
import ru.practicum.ewm.service.dto.category.CategoryDto;
import ru.practicum.ewm.service.dto.category.NewCategoryDto;
import ru.practicum.ewm.service.dto.compilation.CompilationDto;
import ru.practicum.ewm.service.dto.compilation.NewCompilationDto;
import ru.practicum.ewm.service.dto.compilation.UpdateCompilationRequestDto;
import ru.practicum.ewm.service.dto.event.EventFullDto;
import ru.practicum.ewm.service.dto.user.NewUserRequestDto;
import ru.practicum.ewm.service.dto.user.UserDto;
import ru.practicum.ewm.service.model.State;
import ru.practicum.ewm.service.dto.event.UpdateEventAdminRequestDto;

import java.time.LocalDateTime;
import java.util.List;

@Service
public interface AdminService {

    /** Admin: Пользователи */
    List<UserDto> getUsers(List<Integer> ids, int from, int size);

    UserDto addUser(NewUserRequestDto newUser);

    void deleteUser(int userId);

    /** Admin: Категории */
    CategoryDto addCategory(NewCategoryDto cat);

    void deleteCategory(int catId);

    CategoryDto updateCategory(CategoryDto cat);

    /** Admin: События */
    List<EventFullDto> getEvents(List<Integer> users, List<State> states, List<Integer> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size);

    EventFullDto updateEvent(int eventId, UpdateEventAdminRequestDto eventUpdate);

    /** Admin: Подборки событий */
    CompilationDto addCompilation(NewCompilationDto newCompilationDto);

    void deleteCompilation(int compId);

    CompilationDto updateCompilation(int compId, UpdateCompilationRequestDto compilationUpdate);
}
