package ru.practicum.shareit.user.server;

import org.springframework.transaction.annotation.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.NewUserRequest;
import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.module.User;
import ru.practicum.shareit.user.repository.UserStorage;

@AllArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserServicelmpl implements UserService {

    private final UserStorage userStorage;

    @Transactional
    public UserDto create(NewUserRequest request) {
        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email не может быть пустым");
        }
        if (request.getName() == null || request.getName().isBlank()) {
            throw new IllegalArgumentException("Name не может быть пустым");
        }
        if (userStorage.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Пользователь с таким email уже существует");
        }
        User user = UserMapper.mapToUser(request);
        User saved = userStorage.save(user); // Hibernate присвоит ID
        return UserMapper.toUserDto(saved);
    }

    @Transactional
    public UserDto update(Long userId, UpdateUserRequest request) {
        if (request == null) {
            throw new NotFoundException("Пользователь не найден");
        }
        User user = userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id=" + userId + " не найден"));
        UserMapper.updateUserFields(user, request);
        return UserMapper.toUserDto(userStorage.save(user));
    }

    public UserDto getById(Long userId) {
        if (userId == null) {
            throw new NotFoundException("Пользователь c таким id " + userId + " не найден");
        }
        return UserMapper.toUserDto(userStorage.getById(userId));
    }

    @Transactional
    public UserDto delete(Long userId) {
        User user = userStorage.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь с id " + userId + " не найден"));
        userStorage.delete(user);
        return UserMapper.toUserDto(user);
    }
}



