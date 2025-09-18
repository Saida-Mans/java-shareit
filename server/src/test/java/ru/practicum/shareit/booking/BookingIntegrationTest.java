package ru.practicum.shareit.booking;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.module.Booking;
import ru.practicum.shareit.booking.module.Status;
import ru.practicum.shareit.booking.repository.BookingStorage;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.TestExceptionHandler;
import ru.practicum.shareit.item.module.Item;
import ru.practicum.shareit.item.repository.ItemStorage;
import ru.practicum.shareit.user.module.User;
import ru.practicum.shareit.user.repository.UserStorage;
import java.time.LocalDateTime;
import java.util.List;
import ru.practicum.shareit.booking.dto.BookingResponseDto;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Import(TestExceptionHandler.class)
public class BookingIntegrationTest {

    @Autowired
    private BookingStorage bookingStorage;

    @Autowired
    private BookingService bookingService;

    @Autowired
    ItemStorage itemStorage;

    @Autowired
    private UserStorage userStorage;

    private User user;
    private Item item;

    @BeforeEach
    void setup() {
        user = new User();
        user.setName("Тестовый пользователь");
        user.setEmail("test@example.com");
        userStorage.save(user);

        item = new Item();
        item.setName("Дрель");
        item.setDescription("Электрическая дрель");
        item.setAvailable(true);
        item.setOwner(user);
        itemStorage.save(item);
    }

    @Test
    void getBookingsByUser() {
        User user = new User();
        user.setName("Алёна Борматова");
        user.setEmail("alena@gmail.com");
        userStorage.save(user);
        Item item = new Item();
        item.setName("Чайник");
        item.setDescription("Электрический");
        item.setAvailable(true);
        item.setOwner(user);
        itemStorage.save(item);
        Booking pastBooking = new Booking();
        pastBooking.setStart(LocalDateTime.now().minusMonths(1));
        pastBooking.setEnd(LocalDateTime.now().minusWeeks(2));
        pastBooking.setItem(item);
        pastBooking.setBooker(user);
        pastBooking.setStatus(Status.APPROVED);
        bookingStorage.save(pastBooking);
    }

    @Test
    void createBooking_success() {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(item.getId());
        bookingDto.setStart(LocalDateTime.now().plusDays(1));
        bookingDto.setEnd(LocalDateTime.now().plusDays(2));

        BookingResponseDto response = bookingService.create(user.getId(), bookingDto);

        assertNotNull(response);
        assertEquals(item.getId(), response.getItem().getId());
        assertEquals(user.getId(), response.getBooker().getId());
        assertEquals(Status.WAITING, response.getStatus());
    }

    @Test
    void getBookingsByUser_filterPastFutureCurrent() {
        BookingDto pastBooking = new BookingDto();
        pastBooking.setItemId(item.getId());
        pastBooking.setStart(LocalDateTime.now().minusDays(5));
        pastBooking.setEnd(LocalDateTime.now().minusDays(1));
        bookingService.create(user.getId(), pastBooking);
        List<BookingResponseDto> past = bookingService.getBookingsByUser(user.getId(), "PAST");
        assertFalse(past.isEmpty());
        assertTrue(past.stream().allMatch(b -> b.getEnd().isBefore(LocalDateTime.now())));

        List<BookingResponseDto> future = bookingService.getBookingsByUser(user.getId(), "FUTURE");
        assertTrue(future.stream().allMatch(b -> b.getStart().isAfter(LocalDateTime.now())));
    }

    @Test
    void updateBooking_approveAndReject() {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(item.getId());
        bookingDto.setStart(LocalDateTime.now().plusDays(1));
        bookingDto.setEnd(LocalDateTime.now().plusDays(2));

        BookingResponseDto booking = bookingService.create(user.getId(), bookingDto);

        BookingResponseDto approved = bookingService.update(user.getId(), true, booking.getId());
        assertEquals(Status.APPROVED, approved.getStatus());

        BookingResponseDto rejected = bookingService.update(user.getId(), false, booking.getId());
        assertEquals(Status.REJECTED, rejected.getStatus());
    }

    @Test
    void getBooking_accessDenied() {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(item.getId());
        bookingDto.setStart(LocalDateTime.now().plusDays(1));
        bookingDto.setEnd(LocalDateTime.now().plusDays(2));
        BookingResponseDto booking = bookingService.create(user.getId(), bookingDto);

        User otherUser = new User();
        otherUser.setName("Другой пользователь");
        otherUser.setEmail("other@example.com");
        userStorage.save(otherUser);

        assertThrows(RuntimeException.class, () ->
                bookingService.getBooking(otherUser.getId(), booking.getId()));
    }

    @Test
    void createBooking_itemNotAvailable_shouldThrow() {
        item.setAvailable(false);
        itemStorage.save(item);

        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(item.getId());
        bookingDto.setStart(LocalDateTime.now().plusDays(1));
        bookingDto.setEnd(LocalDateTime.now().plusDays(2));

        assertThrows(RuntimeException.class, () ->
                bookingService.create(user.getId(), bookingDto));
    }

    @Test
    void create_itemNotFound_shouldThrow() {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(999L);
        assertThrows(RuntimeException.class, () ->
                bookingService.create(user.getId(), bookingDto));
    }

    @Test
    void getBooking_byOwner_shouldSucceed() {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(item.getId());
        bookingDto.setStart(LocalDateTime.now().plusDays(1));
        bookingDto.setEnd(LocalDateTime.now().plusDays(2));
        BookingResponseDto booking = bookingService.create(user.getId(), bookingDto);
        BookingResponseDto result = bookingService.getBooking(user.getId(), booking.getId());
        assertEquals(booking.getId(), result.getId());
    }

    @Test
    void updateBooking_byNonOwner_shouldThrow() {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(item.getId());
        bookingDto.setStart(LocalDateTime.now().plusDays(1));
        bookingDto.setEnd(LocalDateTime.now().plusDays(2));
        BookingResponseDto booking = bookingService.create(user.getId(), bookingDto);

        User otherUser = new User();
        otherUser.setName("Другой пользователь");
        otherUser.setEmail("other@example.com");
        userStorage.save(otherUser);

        assertThrows(RuntimeException.class, () ->
                bookingService.update(otherUser.getId(), true, booking.getId()));
    }

    @Test
    void getBookingsByUser_noBookings_shouldReturnEmpty() {
        List<BookingResponseDto> bookings = bookingService.getBookingsByUser(999L, "ALL");
        assertTrue(bookings.isEmpty());
    }

    @Test
    void getBookingsByOwner_noItems_shouldThrow() {
        User noItemUser = new User();
        noItemUser.setName("Нет вещей");
        noItemUser.setEmail("empty@example.com");
        userStorage.save(noItemUser);

        assertThrows(RuntimeException.class, () ->
                bookingService.getBookingsByOwner(noItemUser.getId(), "ALL"));
    }
}
