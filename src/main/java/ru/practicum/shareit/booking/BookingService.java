package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;

import java.util.List;

public interface BookingService {

    BookingResponseDto create(Long bookerId, BookingDto bookingDto);

    BookingResponseDto update(Long bookerId, boolean approved, Long bookingId);

    BookingResponseDto getBooking(Long userId, Long bookingId);

    List<BookingResponseDto> getBookingsByUser(Long userId, String state);

    List<BookingResponseDto> getBookingsByOwner(Long userId, String state);
}
