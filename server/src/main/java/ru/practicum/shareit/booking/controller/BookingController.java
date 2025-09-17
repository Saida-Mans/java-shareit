package ru.practicum.shareit.booking.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.service.BookingService;
import java.util.List;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping(path = "/bookings")
@AllArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    public static final String USER_ID_HEADER = "X-Sharer-User-Id";

   @PostMapping
    public BookingResponseDto create(@RequestHeader(USER_ID_HEADER) Long bookerId, @RequestBody BookingDto bookingDto) {
       return bookingService.create(bookerId, bookingDto);
   }

   @PatchMapping("/{bookingId}")
   public BookingResponseDto update(@PathVariable Long bookingId, @RequestParam boolean approved, @RequestHeader(USER_ID_HEADER) Long ownerId) {
       return bookingService.update(ownerId, approved, bookingId);
   }

   @GetMapping("/{bookingId}")
   public BookingResponseDto getBooking(@PathVariable Long bookingId, @RequestHeader(USER_ID_HEADER) Long userId) {
       return bookingService.getBooking(userId, bookingId);
   }

    @GetMapping("/owner")
    public List<BookingResponseDto> getBookingsByOwner(@RequestHeader(USER_ID_HEADER) Long userId, @RequestParam(defaultValue = "ALL") String state) {
        return bookingService.getBookingsByOwner(userId, state);

    }

    @GetMapping
    public List<BookingResponseDto> getBookingsByUser(@RequestHeader(USER_ID_HEADER) Long userId, @RequestParam(defaultValue = "ALL") String state) {
        return bookingService.getBookingsByUser(userId, state);
    }
}
