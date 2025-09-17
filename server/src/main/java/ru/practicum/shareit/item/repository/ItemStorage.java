package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.module.Item;

import java.util.List;

public interface ItemStorage extends JpaRepository<Item, Long> {

    List<Item> findByOwnerId(Long userId);

    @Query("SELECT i FROM Item i " +
            "WHERE i.isAvailable = TRUE AND " +
            "(LOWER(i.name) LIKE LOWER(CONCAT('%', ?1, '%')) " +
            "OR LOWER(i.description) LIKE LOWER(CONCAT('%', ?1, '%')))")
    List<Item> search(String text);

    List<Item> findByRequest_Id(Long requestId);
}

