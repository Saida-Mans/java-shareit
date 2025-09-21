package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
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
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.exception.TestExceptionHandler;

@WebMvcTest(controllers = BookingController.class)
@Import(TestExceptionHandler.class)
public class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService bookingService;

    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @Test
    void createBookingTest() throws Exception {
        BookingDto bookingDto = new BookingDto();
        BookingResponseDto responseDto = new BookingResponseDto();
        responseDto.setId(1L);
        when(bookingService.create(1L, bookingDto)).thenReturn(responseDto);

        mockMvc.perform(post("/bookings")
                        .header(USER_ID_HEADER, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(bookingService).create(1L, bookingDto);
    }

    @Test
    void updateBookingTest() throws Exception {
        BookingResponseDto responseDto = new BookingResponseDto();
        responseDto.setId(2L);
        when(bookingService.update(1L, true, 2L)).thenReturn(responseDto);
        mockMvc.perform(patch("/bookings/2")
                        .header(USER_ID_HEADER, 1L)
                        .param("approved", "true")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2));
        verify(bookingService).update(1L, true, 2L);
    }

    @Test
    void getBookingTest() throws Exception {
        BookingResponseDto responseDto = new BookingResponseDto();
        responseDto.setId(3L);
        when(bookingService.getBooking(1L, 3L)).thenReturn(responseDto);

        mockMvc.perform(get("/bookings/3")
                        .header(USER_ID_HEADER, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3));
        verify(bookingService).getBooking(1L, 3L);
    }

    @Test
    void getBookingsByUserTest() throws Exception {
        BookingResponseDto booking1 = new BookingResponseDto();
        booking1.setId(5L);
        BookingResponseDto booking2 = new BookingResponseDto();
        booking2.setId(6L);
        List<BookingResponseDto> bookings = List.of(booking1, booking2);
        when(bookingService.getBookingsByUser(1L, "ALL")).thenReturn(bookings);
        mockMvc.perform(get("/bookings")
                        .header(USER_ID_HEADER, 1L)
                        .param("state", "ALL")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(5))
                .andExpect(jsonPath("$[1].id").value(6));
        verify(bookingService).getBookingsByUser(1L, "ALL");
    }

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

    @Test
    void createBookingWithoutUserIdShouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getBookingsByUserWithDifferentState() throws Exception {
        List<BookingResponseDto> bookings = List.of();
        when(bookingService.getBookingsByUser(1L, "PAST")).thenReturn(bookings);
        mockMvc.perform(get("/bookings")
                        .header(USER_ID_HEADER, 1L)
                        .param("state", "PAST")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
        verify(bookingService).getBookingsByUser(1L, "PAST");
    }

    @Test
    void updateBookingThrowsException() throws Exception {
        when(bookingService.update(1L, true, 2L))
                .thenThrow(new RuntimeException("Some error"));

        mockMvc.perform(patch("/bookings/2")
                        .header(USER_ID_HEADER, 1L)
                        .param("approved", "true")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }
    }
