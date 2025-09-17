package ru.practicum.shareit.booking.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.BookingClient;
import ru.practicum.shareit.booking.dto.BookingDto;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping(path = "/bookings")
@AllArgsConstructor
public class BookingController {

    private final BookingClient bookingClient;

    public static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader(USER_ID_HEADER) Long bookerId,
                                         @Valid @RequestBody BookingDto bookingDto) {
        return bookingClient.create(bookerId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> update(@PathVariable Long bookingId,
                                         @RequestParam boolean approved,
                                         @RequestHeader(USER_ID_HEADER) Long ownerId) {
        return bookingClient.update(ownerId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBooking(@PathVariable Long bookingId,
                                             @RequestHeader(USER_ID_HEADER) Long userId) {
        return bookingClient.getBooking(userId, bookingId);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getBookingsByOwner(@RequestHeader(USER_ID_HEADER) Long userId,
                                     @RequestParam(defaultValue = "ALL") String state) {
        return bookingClient.getBookingsByOwner(userId, state);
    }

    @GetMapping
    public ResponseEntity<Object> getBookingsByUser(@RequestHeader(USER_ID_HEADER) Long userId,
                                     @RequestParam(defaultValue = "ALL") String state) {
        return bookingClient.getBookingsByUser(userId, state);
    }
}
