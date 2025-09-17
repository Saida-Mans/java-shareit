package ru.practicum.shareit.user;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.module.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.mapper.RequestMapper;
import ru.practicum.shareit.request.module.Request;
import ru.practicum.shareit.user.dto.NewUserRequest;
import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.module.User;

import java.time.LocalDateTime;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

public class UserMapperUnitTest {

    @Test
    void mapToRequest_shouldMapFieldsCorrectly() {
        // given
        Long userId = 10L;
        RequestDto requestDto = new RequestDto();
        requestDto.setDescription("Нужен дрель");
        Request request = RequestMapper.mapToRequest(userId, requestDto);
        assertThat(request.getUserId()).isEqualTo(userId);
        assertThat(request.getDescription()).isEqualTo("Нужен дрель");
        assertThat(request.getCreated()).isNotNull();
        assertThat(request.getCreated()).isBeforeOrEqualTo(LocalDateTime.now());
    }

    @Test
    void toItemRequestDto_shouldMapFieldsAndItemsCorrectly() {
        Request request = new Request();
        request.setId(5L);
        request.setUserId(20L);
        request.setDescription("Нужна лестница");
        request.setCreated(LocalDateTime.now());
        Item item = new Item();
        item.setId(100L);
        item.setName("Лестница");
        item.setDescription("Хозяйственная лестница");
        item.setAvailable(true);
        ItemRequestDto dto = RequestMapper.toItemRequestDto(request, List.of(item));
        assertThat(dto.getId()).isEqualTo(5L);
        assertThat(dto.getUserId()).isEqualTo(20L);
        assertThat(dto.getDescription()).isEqualTo("Нужна лестница");
        assertThat(dto.getCreated()).isEqualTo(request.getCreated());
        assertThat(dto.getItems()).hasSize(1);
        ItemDto mappedItem = dto.getItems().get(0);
        assertThat(mappedItem.getId()).isEqualTo(100L);
        assertThat(mappedItem.getName()).isEqualTo("Лестница");
        assertThat(mappedItem.getDescription()).isEqualTo("Хозяйственная лестница");
        assertThat(mappedItem.getAvailable()).isTrue();
    }

    @Test
    void toUserDto_shouldMapAllFields() {
        User user = new User();
        user.setId(1L);
        user.setName("John Doe");
        user.setEmail("john@example.com");

        UserDto dto = UserMapper.toUserDto(user);

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getName()).isEqualTo("John Doe");
        assertThat(dto.getEmail()).isEqualTo("john@example.com");
    }

    @Test
    void mapToUser_shouldMapAllFields() {
        NewUserRequest request = new NewUserRequest();
        request.setName("Alice");
        request.setEmail("alice@example.com");

        User user = UserMapper.mapToUser(request);

        assertThat(user.getName()).isEqualTo("Alice");
        assertThat(user.getEmail()).isEqualTo("alice@example.com");
    }

    @Test
    void updateUserFields_shouldUpdateAllNonNullFields() {
        User user = new User();
        user.setName("Old Name");
        user.setEmail("old@example.com");

        UpdateUserRequest request = new UpdateUserRequest();
        request.setName("New Name");
        request.setEmail("new@example.com");

        User updatedUser = UserMapper.updateUserFields(user, request);

        assertThat(updatedUser.getName()).isEqualTo("New Name");
        assertThat(updatedUser.getEmail()).isEqualTo("new@example.com");
    }

    @Test
    void updateUserFields_shouldNotUpdateWhenFieldsNullOrBlank() {
        User user = new User();
        user.setName("Original Name");
        user.setEmail("original@example.com");

        UpdateUserRequest request = new UpdateUserRequest();
        request.setName(null);
        request.setEmail("  "); // пустая строка

        User updatedUser = UserMapper.updateUserFields(user, request);

        assertThat(updatedUser.getName()).isEqualTo("Original Name");
        assertThat(updatedUser.getEmail()).isEqualTo("original@example.com");
    }
}
