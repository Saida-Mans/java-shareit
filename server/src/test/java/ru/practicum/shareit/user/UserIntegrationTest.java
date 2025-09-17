package ru.practicum.shareit.user;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.user.server.UserService;
import ru.practicum.shareit.user.dto.NewUserRequest;
import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.dto.UserDto;
import static org.assertj.core.api.Assertions.assertThat;
import ru.practicum.shareit.user.repository.UserStorage;

@SpringBootTest
@Transactional
public class UserIntegrationTest {

    @Autowired
    private UserService userService;
    private UserStorage userStorage;

    @Test
    void testCreateUser() {
        NewUserRequest newUser = new NewUserRequest();
        newUser.setName("Alex");
        newUser.setEmail("alex@example.com");

        UserDto created = userService.create(newUser);

        assertThat(created).isNotNull();
        assertThat(created.getId()).isNotNull();
        assertThat(created.getName()).isEqualTo("Alex");
        assertThat(created.getEmail()).isEqualTo("alex@example.com");
    }

    @Test
    void testUpdateUser() {
        NewUserRequest newUser = new NewUserRequest();
        newUser.setName("Alex");
        newUser.setEmail("alex@example.com");
        UserDto created = userService.create(newUser);
        Long userId = created.getId();
        UpdateUserRequest updateUser = new UpdateUserRequest();
        updateUser.setName("Alex");
        updateUser.setEmail("alexCh@example.com");
        UserDto updated = userService.update(userId, updateUser);
        assertThat(updated).isNotNull();
        assertThat(updated.getId()).isNotNull();
        assertThat(updated.getName()).isEqualTo("Alex");
        assertThat(updated.getEmail()).isEqualTo("alexCh@example.com");
    }
}
