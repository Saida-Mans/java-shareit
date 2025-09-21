package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.server.RequestService;
import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    public static final String USER_ID_HEADER = "X-Sharer-User-Id";

    private final RequestService requestService;

    @PostMapping
    public ItemRequestDto create(@RequestHeader(value = USER_ID_HEADER, required = false) Long userId,
                                 @RequestBody RequestDto requestDto) {
        return requestService.create(userId, requestDto);
    }

    @GetMapping
    public List<ItemRequestDto> findRequest(@RequestHeader(USER_ID_HEADER) Long userId) {
        return requestService.findRequest(userId);
    }

    @GetMapping("{requestId}")
    public ItemRequestDto findById(@RequestHeader(USER_ID_HEADER) Long userId, @PathVariable Long requestId) {
        return requestService.findById(userId, requestId);
    }
}
