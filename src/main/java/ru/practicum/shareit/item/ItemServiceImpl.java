package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserStorage;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ItemServiceImpl implements ItemService {

    private final UserStorage userStorage;
    private final ItemStorage repository;

    @Override
    public ItemDto create(Long id, ItemDto itemDto) {
        if (id == null) {
            throw new IllegalArgumentException("X-Sharer-User-Id не передан");
        }
        User owner = userStorage.getById(id);
        if (owner == null) {
            throw new NotFoundException("User id = " + id + " не найден");
        }
        Item item = ItemMapper.toItem(itemDto);
        item.setOwner(owner);
        repository.create(item);
        return ItemMapper.toItemDto(item);
    }

    @Override
    public ItemDto update(Long userId, Long itemId, ItemDto itemDto) {
        Item item = repository.getById(itemId);
        if (item == null) {
            throw new NotFoundException("Item id = " + itemId + " не найден");
        }
        if (!item.getOwner().getId().equals(userId)) {
            throw new NotFoundException("Владелец с таким id " + userId + "не найден");
        }
        ItemMapper.updateItemFromDto(itemDto, item);
        repository.update(item);
        return ItemMapper.toItemDto(item);
    }

    @Override
    public ItemDto itemById(Long userId, Long itemId) {
        User owner = userStorage.getById(userId);
        if (owner == null) {
            throw new NotFoundException("User id = " + userId + " не найден");
        }
        Item item = repository.getById(itemId);
        if (item == null) {
            throw new NotFoundException("Item id=" + itemId + " не найден");
        }
        return ItemMapper.toItemDto(item);
    }

    @Override
    public List<ItemDto> findAll(long userId) {
        if (userStorage.getById(userId) == null) {
            throw new NotFoundException("User id=" + userId + " не найден");
        }
        return   repository.findAll(userId).stream()
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
    }

