package ru.practicum.ewm.service.service;

import org.springframework.stereotype.Service;
import ru.practicum.ewm.service.dto.compilation.CompilationDto;
import ru.practicum.ewm.service.dto.compilation.NewCompilationDto;
import ru.practicum.ewm.service.dto.compilation.UpdateCompilationRequest;
import ru.practicum.ewm.service.dto.event.State;
import ru.practicum.ewm.service.dto.event.UpdateEventAdminRequest;
import ru.practicum.ewm.service.model.Category;
import ru.practicum.ewm.service.model.Event;
import ru.practicum.ewm.service.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Service
public interface AdminService {

    //------------------------------------------------Admin: Пользователи-----------------------------------------------
    List<User> getUsers(List<Integer> ids, int from, int size);

    User addUser(User newUser);

    void deleteUser(int userId);

    //-------------------------------------------------Admin: Категории-------------------------------------------------
    Category addCategory(Category cat);

    void deleteCategory(int catId);

    Category updateCategory(Category cat);

    //--------------------------------------------------Admin: События--------------------------------------------------
    List<Event> getEvents(List<Integer> users, List<State> states, List<Integer> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size);

    Event updateEvent(int eventId, UpdateEventAdminRequest eventUpdate);

    //----------------------------------------------Admin: Подборки событий---------------------------------------------
    CompilationDto addCompilation(NewCompilationDto newCompilationDto);

    void deleteCompilation(int compId);

    CompilationDto updateCompilation(int compId, UpdateCompilationRequest compilationUpdate);
}
