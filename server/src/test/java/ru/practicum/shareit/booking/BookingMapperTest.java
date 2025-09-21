package ru.practicum.shareit.booking;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.module.Booking;
import ru.practicum.shareit.booking.module.Status;
import ru.practicum.shareit.item.module.Item;
import ru.practicum.shareit.user.module.User;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

public class BookingMapperTest {
    @Test
    void toBookingDto() {
        User booker = new User();
        booker.setId(1L);
        Item item = new Item();
        item.setId(10L);

        Booking booking = new Booking();
        booking.setId(100L);
        booking.setStart(LocalDateTime.now());
        booking.setEnd(LocalDateTime.now().plusDays(1));
        booking.setBooker(booker);
        booking.setItem(item);
        booking.setStatus(Status.APPROVED);
        BookingDto dto = BookingMapper.toBookingDto(booking);
        assertEquals(100L, dto.getId());
        assertEquals(booking.getStart(), dto.getStart());
        assertEquals(booking.getEnd(), dto.getEnd());
        assertEquals(1L, dto.getBooker());
        assertEquals(10L, dto.getItemId());
        assertEquals(Status.APPROVED, dto.getStatus());
    }

    @Test
    void mapToBooking() {
        BookingDto dto = new BookingDto();
        dto.setId(200L);
        dto.setStart(LocalDateTime.now());
        dto.setEnd(LocalDateTime.now().plusDays(2));
        User booker = new User();
        booker.setId(2L);
        Item item = new Item();
        item.setId(20L);
        Booking booking = BookingMapper.mapToBooking(dto, item, booker);
        assertEquals(200L, booking.getId());
        assertEquals(dto.getStart(), booking.getStart());
        assertEquals(dto.getEnd(), booking.getEnd());
        assertEquals(item, booking.getItem());
        assertEquals(booker, booking.getBooker());
        assertEquals(Status.WAITING, booking.getStatus());
    }

    @Test
    void toBookingResponseDto() {
        User booker = new User();
        booker.setId(3L);
        booker.setName("Пётр");
        Item item = new Item();
        item.setId(30L);
        item.setName("Дрель");
        Booking booking = new Booking();
        booking.setId(300L);
        booking.setStart(LocalDateTime.now());
        booking.setEnd(LocalDateTime.now().plusHours(5));
        booking.setBooker(booker);
        booking.setItem(item);
        booking.setStatus(Status.REJECTED);
        BookingResponseDto responseDto = BookingMapper.toBookingResponseDto(booking);
        assertEquals(300L, responseDto.getId());
        assertEquals(booking.getStart(), responseDto.getStart());
        assertEquals(booking.getEnd(), responseDto.getEnd());
        assertEquals(3L, responseDto.getBooker().getId());
        assertEquals("Пётр", responseDto.getBooker().getName());
        assertEquals(30L, responseDto.getItem().getId());
        assertEquals("Дрель", responseDto.getItem().getName());
        assertEquals(Status.REJECTED, responseDto.getStatus());
    }
}


