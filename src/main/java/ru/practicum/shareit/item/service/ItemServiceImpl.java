package ru.practicum.shareit.item.service;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.repository.BookingStorage;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.repository.ItemStorage;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithCommentDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserStorage;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ItemServiceImpl implements ItemService {

    private final UserStorage userStorage;
    private final ItemStorage repository;
    private final CommentRepository commentRepository;
    private final BookingStorage bookingStorage;

    @Transactional
    @Override
    public ItemDto create(Long id, ItemDto itemDto) {
        if (id == null) {
            throw new IllegalArgumentException("X-Sharer-User-Id не передан");
        }
        User owner = userStorage.findById(id)
                .orElseThrow(() -> new NotFoundException("User id = " + id + " не найден"));
        Item item = ItemMapper.toItem(itemDto);
        item.setOwner(owner);
        repository.save(item);
        return ItemMapper.toItemDto(item);
    }

    @Transactional
    @Override
    public ItemDto update(Long userId, Long itemId, ItemDto itemDto) {
        Item item = repository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item с id=" + itemId + " не найден"));
        if (!item.getOwner().getId().equals(userId)) {
            throw new NotFoundException("Владелец с таким id " + userId + "не найден");
        }
        ItemMapper.updateItemFromDto(itemDto, item);
        repository.save(item);
        return ItemMapper.toItemDto(item);
    }

    @Override
    public ItemWithCommentDto itemById(Long userId, Long itemId) {
        User owner = userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("User id=" + userId + " не найден"));

        Item item = repository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item id=" + itemId + " не найден"));
        List<Comment> comments = commentRepository.findAllByItemIdOrderByCreatedDesc(itemId);
        List<CommentDto> commentDtos = comments.stream()
                .map(ItemMapper::toCommentDto)
                .collect(Collectors.toList());
        LocalDateTime now = LocalDateTime.now();
        Booking lastBooking = bookingStorage.findFirstByItemIdAndEndBeforeOrderByEndDesc(itemId, now)
                .orElse(null);
        Booking nextBooking = bookingStorage.findFirstByItemIdAndStartAfterOrderByStartAsc(itemId, now)
                .orElse(null);
        return new ItemWithCommentDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.isAvailable(),
                null,
                nextBooking != null ? BookingMapper.toBookingDto(nextBooking) : null,
                commentDtos);
    }

    @Override
    public List<ItemDto> findAll(long userId) {
        userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("User id=" + userId + " не найден"));
        return   repository. findByOwnerId(userId).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> search(String text) {
        if (text == null || text.isBlank()) {
            return Collections.emptyList();
        }
        return repository.search(text).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public CommentDto createComment(Long userId, Long itemId, CommentDto commentDto) {
        User author = userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id=" + userId + " не найден"));

        Item item = repository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item id=" + itemId + " не найден"));
        boolean booking = bookingStorage.existsApprovedBooking(itemId, userId, Status.APPROVED);
        if (!booking) {
            throw new IllegalArgumentException("Cannot comment on unapproved booking");
        }
        Comment comment = ItemMapper.toComment(commentDto, author, item);
        comment.setCreated(LocalDateTime.now());
        Comment saved = commentRepository.save(comment);
        return ItemMapper.toCommentDto(saved);
    }
}

