package ru.practicum.shareit.user.dto;

import lombok.Data;

@Data
public class UpdateUserRequest {

    private int id;

    private String name;

    private String email;
}
