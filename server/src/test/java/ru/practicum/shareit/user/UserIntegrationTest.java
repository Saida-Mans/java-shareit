package ru.practicum.shareit.user;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.exception.NotFoundException;
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

    @Autowired
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

    @Test
    void testGetById() {
        NewUserRequest newUser = new NewUserRequest();
        newUser.setName("Bob");
        newUser.setEmail("bob@example.com");
        UserDto created = userService.create(newUser);

        UserDto fetched = userService.getById(created.getId());
        assertThat(fetched).isNotNull();
        assertThat(fetched.getId()).isEqualTo(created.getId());
        assertThat(fetched.getName()).isEqualTo("Bob");
    }

    @Test
    void testDeleteUser() {
        NewUserRequest newUser = new NewUserRequest();
        newUser.setName("Carol");
        newUser.setEmail("carol@example.com");
        UserDto created = userService.create(newUser);
        Long userId = created.getId();

        UserDto deleted = userService.delete(userId);

        assertThat(deleted).isNotNull();
        assertThat(deleted.getId()).isEqualTo(userId);
        assertThat(userStorage.findById(userId)).isEmpty();
    }

    @Test
    void testCreateUserWithEmptyEmailThrows() {
        NewUserRequest newUser = new NewUserRequest();
        newUser.setName("David");
        newUser.setEmail("");

        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class,
                () -> userService.create(newUser));
    }

    @Test
    void testCreateUserWithEmptyNameThrows() {
        NewUserRequest newUser = new NewUserRequest();
        newUser.setName("");
        newUser.setEmail("david@example.com");

        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class,
                () -> userService.create(newUser));
    }

    @Test
    void testDeleteNotFoundThrows() {
        org.junit.jupiter.api.Assertions.assertThrows(NotFoundException.class,
                () -> userService.delete(999L));
    }
}
