package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class UpdateUserRequest {

    private int id;

    private String name;

    @Email(message = "Электронная почта должна быть в правильном формате")
    private String email;
}
