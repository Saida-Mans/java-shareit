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
import ru.practicum.shareit.item.dto.CommentDto;
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
    void createItemTest() throws Exception {
        ItemDto itemDto = new ItemDto();
        itemDto.setName("New Item");
        itemDto.setDescription("New Description");
        itemDto.setAvailable(true);
        ItemDto responseDto = new ItemDto();
        responseDto.setId(1L);
        responseDto.setName("New Item");
        responseDto.setDescription("New Description");
        responseDto.setAvailable(true);
        when(itemService.create(1L, itemDto)).thenReturn(responseDto);
        mockMvc.perform(post("/items")
                        .header(USER_ID_HEADER, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("New Item"))
                .andExpect(jsonPath("$.description").value("New Description"))
                .andExpect(jsonPath("$.available").value(true));
        verify(itemService).create(1L, itemDto);
    }

    @Test
    void findAllItemsTest() throws Exception {
        ItemDto item1 = new ItemDto();
        item1.setId(1L);
        item1.setName("Item 1");
        ItemDto item2 = new ItemDto();
        item2.setId(2L);
        item2.setName("Item 2");
        when(itemService.findAll(1L)).thenReturn(List.of(item1, item2));
        mockMvc.perform(get("/items")
                        .header(USER_ID_HEADER, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
        verify(itemService).findAll(1L);
    }

    @Test
    void searchItemsTest() throws Exception {
        ItemDto item1 = new ItemDto();
        item1.setId(1L);
        item1.setName("Hammer");
        when(itemService.search("ham")).thenReturn(List.of(item1));
        mockMvc.perform(get("/items/search")
                        .param("text", "ham"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Hammer"));
        verify(itemService).search("ham");
    }

    @Test
    void createCommentTest() throws Exception {
        CommentDto commentDto = new CommentDto();
        commentDto.setText("Great item!");
        CommentDto responseDto = new CommentDto();
        responseDto.setId(1L);
        responseDto.setText("Great item!");
        when(itemService.createComment(1L, 1L, commentDto)).thenReturn(responseDto);
        mockMvc.perform(post("/items/1/comment")
                        .header(USER_ID_HEADER, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.text").value("Great item!"));
        verify(itemService).createComment(1L, 1L, commentDto);
    }

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

    @Test
    void createItem_invalidUser_shouldThrow() throws Exception {
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Test Item");
        when(itemService.create(null, itemDto)).thenThrow(new RuntimeException("User not found"));

        mockMvc.perform(post("/items")
                        .header(USER_ID_HEADER, "")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void updateItem_notFound_shouldThrow() throws Exception {
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Updated");

        when(itemService.update(1L, 999L, itemDto)).thenThrow(new RuntimeException("Item not found"));

        mockMvc.perform(patch("/items/999")
                        .header(USER_ID_HEADER, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void getItemById_notFound_shouldThrow() throws Exception {
        when(itemService.itemById(1L, 999L)).thenThrow(new RuntimeException("Item not found"));

        mockMvc.perform(get("/items/999")
                        .header(USER_ID_HEADER, 1L))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void searchItems_emptyText_shouldReturnEmptyList() throws Exception {
        when(itemService.search("")).thenReturn(List.of());

        mockMvc.perform(get("/items/search")
                        .param("text", ""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void createComment_invalidUserOrItem_shouldThrow() throws Exception {
        CommentDto commentDto = new CommentDto();
        commentDto.setText("Test comment");

        when(itemService.createComment(1L, 999L, commentDto)).thenThrow(new RuntimeException("Item not found"));

        mockMvc.perform(post("/items/999/comment")
                        .header(USER_ID_HEADER, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentDto)))
                .andExpect(status().isInternalServerError());
    }
}
