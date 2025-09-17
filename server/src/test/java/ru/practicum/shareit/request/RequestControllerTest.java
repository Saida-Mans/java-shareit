package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.controller.ItemRequestController;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.server.RequestService;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import java.util.List;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
public class RequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RequestService requestService;

    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @Test
    void findRequestTest() throws Exception {
        ItemRequestDto request1 = new ItemRequestDto();
        request1.setId(1L);
        request1.setDescription("Request 1");

        ItemRequestDto request2 = new ItemRequestDto();
        request2.setId(2L);
        request2.setDescription("Request 2");

        List<ItemRequestDto> requests = List.of(request1, request2);
        when(requestService.findRequest(1L)).thenReturn(requests);

        mockMvc.perform(get("/requests")
                        .header(USER_ID_HEADER, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].description").value("Request 1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].description").value("Request 2"));

        verify(requestService).findRequest(1L);
    }
}
