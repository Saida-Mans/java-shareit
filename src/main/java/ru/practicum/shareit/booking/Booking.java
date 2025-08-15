package ru.practicum.shareit.booking;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Data
public class Booking {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private String item;
    private String booker;
    private Status status;
}

