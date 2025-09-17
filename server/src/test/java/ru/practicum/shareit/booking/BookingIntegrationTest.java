package ru.practicum.shareit.booking;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.module.Booking;
import ru.practicum.shareit.booking.module.Status;
import ru.practicum.shareit.booking.repository.BookingStorage;
import ru.practicum.shareit.item.module.Item;
import ru.practicum.shareit.item.repository.ItemStorage;
import ru.practicum.shareit.user.module.User;
import ru.practicum.shareit.user.repository.UserStorage;
import java.time.LocalDateTime;

@SpringBootTest
@Transactional
public class BookingIntegrationTest {

    @Autowired
    private BookingStorage bookingStorage;

    @Autowired
    ItemStorage itemStorage;

    @Autowired
    private UserStorage userStorage;

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
}
