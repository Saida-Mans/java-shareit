package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithCommentDto;

import java.util.List;

public interface ItemService {

    ItemDto create(Long id, ItemDto item);

   ItemDto update(Long userId, Long itemId, ItemDto itemDto);

    ItemWithCommentDto itemById(Long userId, Long id);

    List<ItemDto> findAll(long userId);

    List<ItemDto> search(String text);

    CommentDto createComment(Long userId, Long itemId, CommentDto commentDto);
}
