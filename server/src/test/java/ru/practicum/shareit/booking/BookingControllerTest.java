package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.controller.BookingController;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.service.BookingService;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.List;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = BookingController.class)
public class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService bookingService;

    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @Test
    void getBookingsByOwnerTest() throws Exception {
        BookingResponseDto booking1 = new BookingResponseDto();
        booking1.setId(1L);
        BookingResponseDto booking2 = new BookingResponseDto();
        booking2.setId(2L);
        List<BookingResponseDto> bookings = List.of(booking1, booking2);
        when(bookingService.getBookingsByOwner(1L, "ALL")).thenReturn(bookings);
        mockMvc.perform(get("/bookings/owner")
                        .header(USER_ID_HEADER, 1L)
                        .param("state", "ALL")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
        verify(bookingService).getBookingsByOwner(1L, "ALL");
    }
}
