package ru.practicum.shareit.item;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;

@Deprecated
@Repository
public class ItemRepository  {

   private Map<Long, Item> items = new HashMap<>();

    public Item create(Item item) {
        item.setId(getNextId());
        items.put(item.getId(), item);
        return item;
    }

    public Item update(Item item) {
        items.put(item.getId(), item);
         return item;
        }

    public Item getById(long id) {
        return items.get(id);
    }

    public List<Item> findAll(long userId) {
        List<Item> listItem = new ArrayList<>();
        for (Long key : items.keySet()) {
            Item value = items.get(key);
            if (value.getOwner().getId() == userId)
        listItem.add(value);
        }
        return listItem;
    }

    public List<Item> search(String text) {
        String lowerText = text.toLowerCase();
        List<Item> listItem = items.values().stream()
                .filter(i -> Boolean.TRUE.equals(i.isAvailable()))
                .filter(i -> (i.getName() != null && i.getName().toLowerCase().contains(lowerText)) || (i.getDescription() != null && i.getDescription().toLowerCase().contains(lowerText)))
                .collect(Collectors.toList());
        return listItem;
    }

    private Long getNextId() {
        Long currentMaxId = items.keySet()
                .stream()
                .max(Long::compareTo)
                .orElse(0L);
        return ++currentMaxId + 1;
    }
}


