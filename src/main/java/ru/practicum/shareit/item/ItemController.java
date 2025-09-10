package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithCommentDto;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@AllArgsConstructor
@RequestMapping("/items")
public class ItemController {

private final ItemService itemService;

public static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @PostMapping
    public ItemDto create(@RequestHeader(value = USER_ID_HEADER, required = false) Long id,
                          @Valid @RequestBody ItemDto item) {
        return itemService.create(id, item);
    }

    @PatchMapping ("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDto update(@RequestHeader(USER_ID_HEADER) Long userId, @PathVariable Long itemId,
                                                     @RequestBody ItemDto itemDto) {
        return itemService.update(userId, itemId, itemDto);
    }

    @GetMapping ("/{itemId}")
    public ItemWithCommentDto itemById(@RequestHeader(value = USER_ID_HEADER, required = true) Long userId, @PathVariable Long itemId) {
        return itemService.itemById(userId, itemId);
    }

    @GetMapping
    public List<ItemDto> findAll(@RequestHeader(USER_ID_HEADER) Long userId) {
        return itemService.findAll(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestParam String text) {
        return itemService.search(text);
    }

   @PostMapping("/{itemId}/comment")
   public CommentDto createComment(@RequestHeader(USER_ID_HEADER) Long userId, @PathVariable Long itemId,
                                @Valid @RequestBody CommentDto commentDto) {
    return itemService.createComment(userId, itemId, commentDto); }
}

