package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class NewUserRequest {
    private String name;

    @Email(message = "Электронная почта должна быть в правильном формате")
    @NotBlank(message = "Поле name не должно быть пустым")
    private String email;
}
