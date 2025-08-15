package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.NewUserRequest;
import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.dto.UserDto;

@AllArgsConstructor
@Service
public class UserServicempl implements UserService {

    private final UserStorage userStorage;

    public UserDto create(NewUserRequest request) {
        String email = request.getEmail();
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email не может быть пустым");
        }
        User user = UserMapper.mapToUser(request);
        return UserMapper.toUserDto(userStorage.create(user));
    }

    public UserDto update(Long userId, UpdateUserRequest request) {
        if (request == null) {
            throw new NotFoundException("Пользователь не найден");
        }
        User user = userStorage.getById(userId);
        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
            String newEmail = request.getEmail();
            boolean emailTaken = userStorage.getEmails().contains(newEmail);
            if (emailTaken) {
                throw new RuntimeException("Email уже зарегистрирован");
            }
            userStorage.getEmails().remove(user.getEmail());
            userStorage.getEmails().add(newEmail);
        }
        UserMapper.updateUserFields(user, request);
        return UserMapper.toUserDto(userStorage.update(user));
    }

    public UserDto getById(Long userId) {
        if (userId == null) {
            throw new NotFoundException("Пользователь c таким id " + userId + " не найден");
        }
        return UserMapper.toUserDto(userStorage.getById(userId));
    }

    public UserDto delete(Long userId) {
        if (userId == null) {
            throw new NotFoundException("Пользователь c таким id " + userId + " не найден");
        }
        return UserMapper.toUserDto(userStorage.delete(userId));
    }
}
