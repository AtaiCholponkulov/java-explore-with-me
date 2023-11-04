package ru.practicum.ewm.service.dto.user;

import lombok.Data;

@Data
public class NewUserRequest {

    private String name;
    private String email;
}
