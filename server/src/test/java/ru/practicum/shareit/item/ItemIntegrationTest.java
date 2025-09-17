package ru.practicum.shareit.item;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithCommentDto;
import ru.practicum.shareit.item.server.ItemService;
import ru.practicum.shareit.user.dto.NewUserRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.server.UserService;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class ItemIntegrationTest {

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserService userService;

    @Test
    void itemById() {
        NewUserRequest newUser = new NewUserRequest();
        newUser.setName("Alice");
        newUser.setEmail("alice@example.com");
        UserDto createdUser = userService.create(newUser);
        Long userId = createdUser.getId();
    ItemDto item = new ItemDto();
    item.setName("Дрель");
    item.setDescription("Чтобы сверлить");
    item.setAvailable(true);
    ItemDto createdItem = itemService.create(userId, item);
    Long itemId = createdItem.getId();
    ItemWithCommentDto itemById = itemService.itemById(userId, itemId);
        assertThat(itemById).isNotNull();
        assertThat(itemById.getId()).isNotNull();
        assertThat(itemById.getName()).isEqualTo("Дрель");
        assertThat(itemById.getDescription()).isEqualTo("Чтобы сверлить");
    }
}
