package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithCommentDto;
import ru.practicum.shareit.item.repository.ItemStorage;
import ru.practicum.shareit.item.server.ItemService;
import java.util.List;

@WebMvcTest(controllers = ItemController.class)
public class ItemControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemService itemService;

    @MockBean
    private ItemStorage itemStorage;

    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @Test
    void updateItemTest() throws Exception {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("Updated Item");
        itemDto.setDescription("Updated Description");
        itemDto.setAvailable(true);
        when(itemService.update(1L, 1L, itemDto)).thenReturn(itemDto);
        mockMvc.perform(patch("/items/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_ID_HEADER, 1L)
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Updated Item"))
                .andExpect(jsonPath("$.description").value("Updated Description"))
                .andExpect(jsonPath("$.available").value(true));

        verify(itemService).update(1L, 1L, itemDto);
    }

    @Test
    void getItemByIdTest() throws Exception {
        ItemWithCommentDto item = new ItemWithCommentDto();
        item.setId(1L);
        item.setName("Item 1");
        item.setDescription("Description 1");
        item.setAvailable(true);
        item.setComments(List.of());
        when(itemService.itemById(1L, 1L)).thenReturn(item);
        mockMvc.perform(get("/items/1")
                        .header(USER_ID_HEADER, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Item 1"))
                .andExpect(jsonPath("$.description").value("Description 1"))
                .andExpect(jsonPath("$.available").value(true))
                .andExpect(jsonPath("$.comments").isArray());
        verify(itemService).itemById(1L, 1L);
    }
}
