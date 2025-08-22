package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import java.util.List;

public interface ItemService {

    ItemDto create(Long id, ItemDto item);

   ItemDto update(Long userId, Long itemId, ItemDto itemDto);

    ItemDto itemById(Long userId, Long id);

    List<ItemDto> findAll(long userId);

    List<ItemDto> search(String text);
}
