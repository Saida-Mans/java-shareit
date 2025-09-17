package ru.practicum.shareit.request.mapper;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.module.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.module.Request;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RequestMapper {

    public static Request mapToRequest(Long userId, RequestDto requestDto) {
        Request request = new Request();
        request.setUserId(userId);
        request.setDescription(requestDto.getDescription());
        request.setCreated(LocalDateTime.now());
        return request;
    }

    public static ItemRequestDto toItemRequestDto(Request request, List<Item> items) {
        ItemRequestDto dto = new ItemRequestDto();
        dto.setId(request.getId());
        dto.setUserId(request.getUserId());
        dto.setDescription(request.getDescription());
        dto.setCreated(request.getCreated());
        List<ItemDto> itemDtos = (items == null ? new ArrayList<>() : items.stream()
                .map(ItemMapper::toItemDto)
                .toList());
        dto.setItems(itemDtos);
        return dto;
    }
}
