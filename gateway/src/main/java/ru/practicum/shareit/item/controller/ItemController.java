package ru.practicum.shareit.item.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.ItemClient;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@AllArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemClient itemClient;

    public static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader(value = USER_ID_HEADER, required = false) Long id,
                                         @Valid @RequestBody ItemDto item) {
        return itemClient.create(id, item);
    }

    @PatchMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> update(@RequestHeader(USER_ID_HEADER) Long userId,
                                         @PathVariable Long itemId,
                                         @RequestBody ItemDto itemDto) {
        return itemClient.update(userId, itemId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> itemById(@RequestHeader(USER_ID_HEADER) Long userId,
                                           @PathVariable Long itemId) {
        return itemClient.getById(userId, itemId);
    }

    @GetMapping
    public ResponseEntity<Object> findAll(@RequestHeader(USER_ID_HEADER) Long userId) {
        return itemClient.findAll(userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItems(@RequestParam String text) {
        return itemClient.search(text);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@RequestHeader(USER_ID_HEADER) Long userId,
                                                @PathVariable Long itemId,
                                                @Valid @RequestBody CommentDto commentDto) {
        return itemClient.addComment(userId, itemId, commentDto);
    }
}

