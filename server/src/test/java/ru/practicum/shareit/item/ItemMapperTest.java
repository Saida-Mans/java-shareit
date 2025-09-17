package ru.practicum.shareit.item;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.module.Comment;
import ru.practicum.shareit.item.module.Item;
import ru.practicum.shareit.request.module.Request;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.user.module.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class ItemMapperTest {

    @Test
    void toItemDto() {
        Item item = new Item();
        item.setId(1L);
        item.setName("Дрель");
        item.setDescription("Электрическая дрель");
        item.setAvailable(true);
        item.setRequest(new Request());
        item.getRequest().setId(10L);
        ItemDto dto = ItemMapper.toItemDto(item);
        assertEquals(1L, dto.getId());
        assertEquals("Дрель", dto.getName());
        assertEquals("Электрическая дрель", dto.getDescription());
        assertTrue(dto.getAvailable());
        assertEquals(10L, dto.getRequestId());
    }

    @Test
    void toItem() {
        ItemDto dto = new ItemDto();
        dto.setId(2L);
        dto.setName("Лопата");
        dto.setDescription("Садовая лопата");
        dto.setAvailable(false);
        Item item = ItemMapper.toItem(dto);
        assertEquals(2L, item.getId());
        assertEquals("Лопата", item.getName());
        assertEquals("Садовая лопата", item.getDescription());
        assertFalse(item.isAvailable());
    }

    @Test
    void updateItemFromDto() {
        Item item = new Item();
        item.setName("Старое имя");
        item.setDescription("Старое описание");
        item.setAvailable(false);
        ItemDto dto = new ItemDto();
        dto.setName("Новое имя");
        dto.setDescription("");
        dto.setAvailable(true);
        ItemMapper.updateItemFromDto(dto, item);
        assertEquals("Новое имя", item.getName());
        assertEquals("Старое описание", item.getDescription());
        assertTrue(item.isAvailable());
    }

    @Test
    void toComment_shouldMapFieldsCorrectly() {
        User author = new User();
        author.setId(1L);
        author.setName("Пользователь");

        Item item = new Item();
        item.setId(2L);

        CommentDto commentDto = new CommentDto();
        commentDto.setText("Комментарий");

        Comment comment = ItemMapper.toComment(commentDto, author, item);

        assertEquals("Комментарий", comment.getText());
        assertEquals(author, comment.getAuthor());
        assertEquals(item, comment.getItem());
        assertNotNull(comment.getCreated());
        assertTrue(comment.getCreated().isBefore(LocalDateTime.now().plusSeconds(1)));
    }

    @Test
    void toCommentDto_shouldMapFieldsCorrectly() {
        User author = new User();
        author.setId(1L);
        author.setName("Пользователь");

        Item item = new Item();
        item.setId(2L);

        Comment comment = new Comment();
        comment.setId(10L);
        comment.setText("Комментарий");
        comment.setAuthor(author);
        comment.setItem(item);
        comment.setCreated(LocalDateTime.now());

        CommentDto dto = ItemMapper.toCommentDto(comment);

        assertEquals(10L, dto.getId());
        assertEquals("Комментарий", dto.getText());
        assertEquals(2L, dto.getItemId());
        assertEquals(1L, dto.getAuthor());
        assertEquals("Пользователь", dto.getAuthorName());
        assertEquals(comment.getCreated(), dto.getCreated());
    }
}
