package ru.practicum.ewm.service.mapper;

import ru.practicum.ewm.service.dto.user.NewUserRequest;
import ru.practicum.ewm.service.dto.user.UserDto;
import ru.practicum.ewm.service.dto.user.UserShortDto;
import ru.practicum.ewm.service.model.User;

import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {

    public static User map(NewUserRequest newUser) {
        return User.builder()
                .email(newUser.getEmail())
                .name(newUser.getName())
                .build();
    }

    public static UserDto mapToDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }

    public static List<UserDto> map(List<User> users) {
        return users.stream()
                .map(UserMapper::mapToDto)
                .collect(Collectors.toList());
    }

    public static UserShortDto mapToShortDto(User user) {
        return UserShortDto.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }
}
