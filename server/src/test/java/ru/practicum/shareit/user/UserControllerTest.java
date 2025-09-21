package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.controller.UserController;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.module.User;
import ru.practicum.shareit.user.repository.UserStorage;
import ru.practicum.shareit.user.server.UserService;
import ru.practicum.shareit.user.dto.NewUserRequest;
import ru.practicum.shareit.user.dto.UpdateUserRequest;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertThrows;

@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    UserStorage userStorage;

    private UserDto user1;
    private UserDto user2;

    @BeforeEach
    public void setUp() {
        user1 = new UserDto();
        user1.setId(1L);
        user1.setName("User 1");
        user1.setEmail("user1@example.com");

        user2 = new UserDto();
        user2.setId(2L);
        user2.setName("User 2");
        user2.setEmail("user2@example.com");
    }

    @Test
    public void testGetByIdUsers() throws Exception {
        when(userService.getById(1L)).thenReturn(user1);
        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("User 1")))
                .andExpect(jsonPath("$.email", is("user1@example.com")));
    }

    @Test
    void deleteUserTest() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setName("User 1");
        user.setEmail("user1@example.com");
        when(userService.delete(1L)).thenReturn(UserMapper.toUserDto(user));
        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("User 1"))
                .andExpect(jsonPath("$.email").value("user1@example.com"));
        verify(userService).delete(1L);
    }

    @Test
    void createUserTest() throws Exception {
        NewUserRequest newUser = new NewUserRequest();
        newUser.setName("New User");
        newUser.setEmail("newuser@example.com");

        UserDto createdUser = new UserDto();
        createdUser.setId(3L);
        createdUser.setName("New User");
        createdUser.setEmail("newuser@example.com");

        when(userService.create(newUser)).thenReturn(createdUser);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"New User\",\"email\":\"newuser@example.com\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.name").value("New User"))
                .andExpect(jsonPath("$.email").value("newuser@example.com"));

        verify(userService).create(newUser);
    }

    @Test
    void updateUserTest() throws Exception {
        UpdateUserRequest updateUser = new UpdateUserRequest();
        updateUser.setName("Updated User");
        updateUser.setEmail("updated@example.com");

        UserDto updatedUser = new UserDto();
        updatedUser.setId(1L);
        updatedUser.setName("Updated User");
        updatedUser.setEmail("updated@example.com");

        when(userService.update(1L, updateUser)).thenReturn(updatedUser);

        mockMvc.perform(patch("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Updated User\",\"email\":\"updated@example.com\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Updated User"))
                .andExpect(jsonPath("$.email").value("updated@example.com"));

        verify(userService).update(1L, updateUser);
    }

    @Test
    void updateUser_emptyBody_shouldThrow() throws Exception {
        mockMvc.perform(patch("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk());
    }

    @Test
    void createUser_invalidEmail_shouldThrow() throws Exception {
        NewUserRequest request = new NewUserRequest();
        request.setName("User");
        request.setEmail("invalid-email");
        when(userService.create(request)).thenThrow(new IllegalArgumentException("Email не может быть пустым"));
        assertThrows(IllegalArgumentException.class, () -> userService.create(request));
    }

    @Test
    void getById_nonExistentUser_shouldThrow() throws Exception {
        when(userService.getById(99L)).thenThrow(new RuntimeException("User not found"));
        mockMvc.perform(get("/users/99"))
                .andExpect(status().is5xxServerError());
    }
}




