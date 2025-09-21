package ru.practicum.shareit.request.server;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.RequestDto;
import java.util.List;

public interface RequestService {

    ItemRequestDto create(Long userId, RequestDto requestDto);

    List<ItemRequestDto> findRequest(Long userId);

    ItemRequestDto findById(Long userId, Long requestId);
}
