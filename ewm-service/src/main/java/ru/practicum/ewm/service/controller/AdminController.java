package ru.practicum.ewm.service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.service.dto.category.CategoryDto;
import ru.practicum.ewm.service.dto.category.NewCategoryDto;
import ru.practicum.ewm.service.dto.compilation.CompilationDto;
import ru.practicum.ewm.service.dto.compilation.NewCompilationDto;
import ru.practicum.ewm.service.dto.compilation.UpdateCompilationRequest;
import ru.practicum.ewm.service.dto.event.EventFullDto;
import ru.practicum.ewm.service.dto.event.State;
import ru.practicum.ewm.service.dto.event.UpdateEventAdminRequest;
import ru.practicum.ewm.service.dto.user.NewUserRequest;
import ru.practicum.ewm.service.dto.user.UserDto;
import ru.practicum.ewm.service.mapper.UserMapper;
import ru.practicum.ewm.service.service.AdminService;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.ewm.service.mapper.CategoryMapper.map;
import static ru.practicum.ewm.service.mapper.EventMapper.mapToFullDto;
import static ru.practicum.ewm.service.mapper.EventMapper.mapToFullDtos;
import static ru.practicum.ewm.service.mapper.UserMapper.map;
import static ru.practicum.ewm.service.valid.Validator.*;

@RestController
@RequestMapping(path = "/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService service;

    //------------------------------------------------Admin: Пользователи-----------------------------------------------
    @GetMapping("/users")
    public List<UserDto> getUsers(@RequestParam(required = false) List<Integer> ids,
                                  @RequestParam(defaultValue = "0") int from,
                                  @RequestParam(defaultValue = "10") int size) {
        validatePaginationParams(from, size);
        return map(service.getUsers(ids, from, size));
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto addUser(@RequestBody NewUserRequest newUser) {
        validateNewUser(newUser);
        return UserMapper.mapToDto(service.addUser(map(newUser)));
    }

    @DeleteMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable int userId) {
        service.deleteUser(userId);
    }

    //-------------------------------------------------Admin: Категории-------------------------------------------------
    @PostMapping("/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto addCategory(@RequestBody NewCategoryDto newCat) {
        validateNewCategory(newCat);
        return map(service.addCategory(map(newCat)));
    }

    @DeleteMapping("/categories/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable int catId) {
        service.deleteCategory(catId);
    }

    @PatchMapping("/categories/{catId}")
    public CategoryDto updateCategory(@RequestBody CategoryDto cat,
                                      @PathVariable int catId) {
        validateUpdateCategory(cat);
        cat.setId(catId);
        return map(service.updateCategory(map(cat)));
    }

    //--------------------------------------------------Admin: События--------------------------------------------------
    @GetMapping("/events")
    public List<EventFullDto> getEvents(@RequestParam(required = false) List<Integer> users,
                                        @RequestParam(required = false) List<State> states,
                                        @RequestParam(required = false) List<Integer> categories,
                                        @RequestParam(required = false) @DateTimeFormat(pattern = DATE_TIME_PATTERN) LocalDateTime rangeStart,
                                        @RequestParam(required = false) @DateTimeFormat(pattern = DATE_TIME_PATTERN) LocalDateTime rangeEnd,
                                        @RequestParam(defaultValue = "0") int from,
                                        @RequestParam(defaultValue = "10") int size) {
        validateRangeStartAndEndParams(rangeStart, rangeEnd);
        validatePaginationParams(from, size);
        return mapToFullDtos(service.getEvents(users, states, categories, rangeStart, rangeEnd, from, size));
    }

    @PatchMapping("/events/{eventId}")
    public EventFullDto updateEvent(@PathVariable int eventId,
                                    @RequestBody UpdateEventAdminRequest eventUpdate) {
        validateEventAdminUpdate(eventUpdate);
        return mapToFullDto(service.updateEvent(eventId, eventUpdate));
    }

    //----------------------------------------------Admin: Подборки событий---------------------------------------------
    @PostMapping("/compilations")
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto addCompilation(@RequestBody NewCompilationDto newCompilationDto) {
        validateNewCompilation(newCompilationDto);
        return service.addCompilation(newCompilationDto);
    }

    @DeleteMapping("/compilations/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable int compId) {
        service.deleteCompilation(compId);
    }

    @PatchMapping("/compilations/{compId}")
    public CompilationDto updateCompilation(@PathVariable int compId,
                                            @RequestBody UpdateCompilationRequest compilationUpdate) {
        validateUpdateCompilationRequest(compilationUpdate);
        return service.updateCompilation(compId, compilationUpdate);
    }
}
