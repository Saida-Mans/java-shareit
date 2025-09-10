package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;

import java.util.List;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping(path = "/bookings")
@AllArgsConstructor
public class BookingController {

    BookingService bookingService;

   @PostMapping
    public BookingResponseDto create(@RequestHeader(value = "X-Sharer-User-Id") Long bookerId, @Valid @RequestBody BookingDto bookingDto) {
       return bookingService.create(bookerId, bookingDto);
   }

   @PatchMapping("/{bookingId}")
   public BookingResponseDto update(@PathVariable Long bookingId, @RequestParam boolean approved, @RequestHeader("X-Sharer-User-Id") Long ownerId) {
       return bookingService.update(ownerId, approved, bookingId);
   }

   @GetMapping("/{bookingId}")
   public BookingResponseDto getBooking(@PathVariable Long bookingId, @RequestHeader("X-Sharer-User-Id") Long userId) {
       return bookingService.getBooking(userId, bookingId);
   }

    @GetMapping("/owner")
    public List<BookingResponseDto> getBookingsByOwner(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam(defaultValue = "ALL") String state) {
        return bookingService.getBookingsByOwner(userId, state);

    }

    @GetMapping
    public List<BookingResponseDto> getBookingsByUser(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestParam(defaultValue = "ALL") String state) {
        return bookingService.getBookingsByUser(userId, state);
    }
}
