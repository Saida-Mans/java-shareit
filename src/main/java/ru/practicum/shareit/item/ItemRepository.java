package ru.practicum.shareit.item;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.*;

@Repository
public class ItemRepository {

    Map<Long, Item> items = new HashMap<>();

    public Item create (Item item) {
        item.setId(getNextId());
        items.put(item.getId(), item);
        return item;
    }

    public Item update (Item item) {
        items.put(item.getId(), item);
         return item;
        }

    public Item getById(long id) {
        return items.get(id);
    }

    public List<Item> findAll(long userId) {
        List<Item> listItem = new ArrayList<>();
        for (Long key : items.keySet()){
            Item value = items.get(key);
            if(value.getOwner().getId() == userId)
        listItem.add(value);
        }
        return listItem;
    }

    public List<Item> search(String text) {
        String lowerText = text.toLowerCase();
        List<Item> listItem = new ArrayList<>();
        for (Item item : items.values()){
            if (Boolean.TRUE.equals(item.isAvailable()) &&
                    (item.getName() != null && item.getName().toLowerCase().contains(lowerText) ||
                            item.getDescription() != null && item.getDescription().toLowerCase().contains(lowerText))) {
                listItem.add(item);
            }
    } return listItem;
    }

    private Long getNextId() {
        Long currentMaxId = items.keySet()
                .stream()
                .max(Long::compareTo)
                .orElse(0L);
        return ++currentMaxId+1;
    }
}


