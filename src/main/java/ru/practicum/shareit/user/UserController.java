package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.NewUserRequest;
import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.dto.UserDto;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@AllArgsConstructor
@RequestMapping(path = "/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserDto create(@Valid @RequestBody NewUserRequest user) {
        return userService.create(user);
    }

    @PatchMapping ("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto update(@PathVariable Long userId,
                          @RequestBody UpdateUserRequest user) {
        return userService.update(userId, user);
    }

    @GetMapping ("/{userId}")
    public UserDto getById(@PathVariable Long userId) {
        return userService.getById(userId);
    }

    @DeleteMapping ("/{userId}")
    public UserDto delete(@PathVariable Long userId) {
        return userService.delete(userId);
    }
}
