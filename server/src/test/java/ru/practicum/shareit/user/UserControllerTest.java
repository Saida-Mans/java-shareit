package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.controller.UserController;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.module.User;
import ru.practicum.shareit.user.repository.UserStorage;
import ru.practicum.shareit.user.server.UserService;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

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
    }




