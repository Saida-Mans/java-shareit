package ru.practicum.shareit.item;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithCommentDto;
import ru.practicum.shareit.item.server.ItemService;
import ru.practicum.shareit.user.dto.NewUserRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.server.UserService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

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

    @Test
    void createItemWithRequest() {
        NewUserRequest newUser = new NewUserRequest();
        newUser.setName("Bob");
        newUser.setEmail("bob@example.com");
        UserDto createdUser = userService.create(newUser);

        ItemDto itemDto = new ItemDto();
        itemDto.setName("Молоток");
        itemDto.setDescription("Для забивания гвоздей");
        itemDto.setAvailable(true);

        ItemDto createdItem = itemService.create(createdUser.getId(), itemDto);

        assertThat(createdItem.getId()).isNotNull();
        assertThat(createdItem.getName()).isEqualTo("Молоток");
        assertThat(createdItem.getDescription()).isEqualTo("Для забивания гвоздей");
        assertThat(createdItem.getAvailable()).isTrue();
    }

    @Test
    void updateItem_success() {
        NewUserRequest newUser = new NewUserRequest();
        newUser.setName("Charlie");
        newUser.setEmail("charlie@example.com");
        UserDto createdUser = userService.create(newUser);

        ItemDto itemDto = new ItemDto();
        itemDto.setName("Лопата");
        itemDto.setDescription("Для копки");
        itemDto.setAvailable(true);
        ItemDto createdItem = itemService.create(createdUser.getId(), itemDto);

        ItemDto updateDto = new ItemDto();
        updateDto.setName("Новая лопата");
        updateDto.setAvailable(false);

        ItemDto updatedItem = itemService.update(createdUser.getId(), createdItem.getId(), updateDto);

        assertThat(updatedItem.getName()).isEqualTo("Новая лопата");
        assertThat(updatedItem.getAvailable()).isFalse();
        assertThat(updatedItem.getDescription()).isEqualTo("Для копки"); // поле без изменений
    }

    @Test
    void findAllAndSearch() {
        NewUserRequest newUser = new NewUserRequest();
        newUser.setName("Dana");
        newUser.setEmail("dana@example.com");
        UserDto createdUser = userService.create(newUser);

        ItemDto item1 = new ItemDto();
        item1.setName("Отвёртка");
        item1.setDescription("Крестовая");
        item1.setAvailable(true);
        itemService.create(createdUser.getId(), item1);

        ItemDto item2 = new ItemDto();
        item2.setName("Дрель");
        item2.setDescription("Электрическая");
        item2.setAvailable(true);
        itemService.create(createdUser.getId(), item2);

        assertThat(itemService.findAll(createdUser.getId())).hasSize(2);
        assertThat(itemService.search("дрель")).hasSize(1);
        assertThat(itemService.search("")).isEmpty();
    }

    @Test
    void createComment_successAndFail() {
        NewUserRequest newUser = new NewUserRequest();
        newUser.setName("Eve");
        newUser.setEmail("eve@example.com");
        UserDto createdUser = userService.create(newUser);

        ItemDto itemDto = new ItemDto();
        itemDto.setName("Пила");
        itemDto.setDescription("Электрическая пила");
        itemDto.setAvailable(true);
        ItemDto createdItem = itemService.create(createdUser.getId(), itemDto);
        CommentDto comment = new CommentDto();
        comment.setText("Хорошая пила");
        assertThatThrownBy(() ->
                itemService.createComment(createdUser.getId(), createdItem.getId(), comment))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Cannot comment on unapproved booking");
    }
}
