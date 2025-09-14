package ru.practicum.shareit.booking.service;

import org.springframework.transaction.annotation.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.repository.BookingStorage;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.repository.ItemStorage;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserStorage;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class BookingServicempl implements BookingService {

    private final BookingStorage bookingStorage;

    private final ItemStorage itemStorage;

    private final UserStorage userStorage;

    @Transactional
    @Override
    public BookingResponseDto create(Long bookerId, BookingDto bookingDto) {
        Item item = itemStorage.findById(bookingDto.getItemId())
                .orElseThrow(() -> new NotFoundException("Item id=" + bookingDto.getItemId() + " не найден"));
        if (!item.isAvailable()) {
            throw new IllegalArgumentException("Item unavailable for booking");
        }
        User user = userStorage.findById(bookerId)
                .orElseThrow(() -> new NotFoundException("User id=" + bookerId + " не найден"));
        Booking booking = BookingMapper.mapToBooking(bookingDto, item, user);
        bookingStorage.save(booking);
        return BookingMapper.toBookingResponseDto(booking);
    }

    @Transactional
    @Override
    public BookingResponseDto update(Long bookerId, boolean approved, Long bookingId) {
        Booking booking = bookingStorage.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking id=" + bookingId + " не найден"));
        if (!booking.getItem().getOwner().getId().equals(bookerId)) {
           throw new IllegalArgumentException("Booking id=" + bookingId + " нельзя одобрить: пользователь не владелец");
        }
        booking.setStatus(approved ? Status.APPROVED : Status.REJECTED);
        bookingStorage.save(booking);
        return BookingMapper.toBookingResponseDto(booking);
    }

    @Override
    public BookingResponseDto getBooking(Long userId, Long bookingId) {
        Booking booking = bookingStorage.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking id=" + bookingId + " не найден"));
        if (!booking.getBooker().getId().equals(userId) &&
                !booking.getItem().getOwner().getId().equals(userId)) {
            throw new AccessDeniedException("Booking id=" + bookingId + " недоступен для пользователя id=" + userId);
        }
        return BookingMapper.toBookingResponseDto(booking);
    }

    @Override
    public List<BookingResponseDto> getBookingsByUser(Long userId, String state) {
        List<Booking> bookings = bookingStorage.findByBookerIdOrderByStartDesc(userId);
        LocalDateTime now = LocalDateTime.now();
        return bookings.stream()
                .filter(booking -> switch (state.toUpperCase()) {
                    case "CURRENT" -> booking.getStart().isBefore(now) && booking.getEnd().isAfter(now);
                    case "PAST" -> booking.getEnd().isBefore(now);
                    case "FUTURE" -> booking.getStart().isAfter(now);
                    case "WAITING" -> booking.getStatus() == Status.WAITING;
                    case "REJECTED" -> booking.getStatus() == Status.REJECTED;
                    default -> true; // ALL
                })
                .map(BookingMapper::toBookingResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingResponseDto> getBookingsByOwner(Long userId, String state) {
        User owner = userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь id=" + userId + " не найден"));
        List<Item> items = itemStorage.findByOwnerId(owner.getId());
        if (items.isEmpty()) {
            throw new AccessDeniedException("У пользователя id=" + userId + " нет вещей для бронирований");
        }
        List<Booking> bookings = bookingStorage.findByItemOwnerIdOrderByStartDesc(userId);
        LocalDateTime now = LocalDateTime.now();
        return bookings.stream()
                .filter(booking -> switch (state.toUpperCase()) {
                    case "CURRENT" -> booking.getStart().isBefore(now) && booking.getEnd().isAfter(now);
                    case "PAST" -> booking.getEnd().isBefore(now);
                    case "FUTURE" -> booking.getStart().isAfter(now);
                    case "WAITING" -> booking.getStatus() == Status.WAITING;
                    case "REJECTED" -> booking.getStatus() == Status.REJECTED;
                    default -> true;
                })
                .map(BookingMapper::toBookingResponseDto)
                .collect(Collectors.toList());
    }
}