package ru.practicum.shareit.booking.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Data
public class BookingDto {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private Long booker;
    private Long itemId;
    private Status status;
}
