package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.NewUserRequest;
import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.dto.UserDto;

public interface UserService {

    UserDto create(NewUserRequest request);

    UserDto update(Long userId, UpdateUserRequest request);

    UserDto getById(Long userId);

    UserDto delete(Long userId);
}
