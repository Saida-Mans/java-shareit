package ru.practicum.shareit.item;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.module.Item;
import ru.practicum.shareit.request.module.Request;
import ru.practicum.shareit.item.mapper.ItemMapper;
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
}
