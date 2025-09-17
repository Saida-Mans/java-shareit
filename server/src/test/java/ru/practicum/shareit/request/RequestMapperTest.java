package ru.practicum.shareit.request;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.module.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.module.Request;
import ru.practicum.shareit.request.mapper.RequestMapper;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class RequestMapperTest {

    @Test
    void mapToRequest() {
        Long userId = 1L;
        RequestDto requestDto = new RequestDto();
        requestDto.setDescription("Нужен молоток");
        Request request = RequestMapper.mapToRequest(userId, requestDto);
        assertEquals(userId, request.getUserId());
        assertEquals("Нужен молоток", request.getDescription());
        assertNotNull(request.getCreated());
        assertTrue(request.getCreated().isBefore(LocalDateTime.now().plusSeconds(1)));
    }

    @Test
    void toItemRequestDto() {
        Request request = new Request();
        request.setId(10L);
        request.setUserId(2L);
        request.setDescription("Нужна лестница");
        request.setCreated(LocalDateTime.now());
        Item item = new Item();
        item.setId(100L);
        item.setName("Лестница");
        item.setDescription("Деревянная лестница");
        item.setAvailable(true);
        ItemRequestDto dto = RequestMapper.toItemRequestDto(request, List.of(item));
        assertEquals(10L, dto.getId());
        assertEquals(2L, dto.getUserId());
        assertEquals("Нужна лестница", dto.getDescription());
        assertEquals(request.getCreated(), dto.getCreated());
        assertEquals(1, dto.getItems().size());
        ItemDto mappedItem = dto.getItems().get(0);
        assertEquals(100L, mappedItem.getId());
        assertEquals("Лестница", mappedItem.getName());
        assertEquals("Деревянная лестница", mappedItem.getDescription());
        assertTrue(mappedItem.getAvailable());
    }
}
