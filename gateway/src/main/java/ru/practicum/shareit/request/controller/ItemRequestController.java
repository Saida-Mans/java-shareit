package ru.practicum.shareit.request.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.ItemRequestClient;
import ru.practicum.shareit.request.dto.RequestDto;

@Controller
@RequestMapping("/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    public static final String USER_ID_HEADER = "X-Sharer-User-Id";

    private final ItemRequestClient requestClient;

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader(value = USER_ID_HEADER, required = false) Long userId,
                                         @Valid @RequestBody RequestDto requestDto) {
        return requestClient.create(userId, requestDto);
    }

    @GetMapping
    public ResponseEntity<Object> findRequest(@RequestHeader(value = USER_ID_HEADER, required = true) Long userId) {
        return requestClient.findRequest(userId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> findById(@RequestHeader(value = USER_ID_HEADER, required = true) Long userId,
                                           @PathVariable Long requestId) {
        return requestClient.findById(userId, requestId);
    }
}
