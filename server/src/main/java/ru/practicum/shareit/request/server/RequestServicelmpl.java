package ru.practicum.shareit.request.server;

import org.springframework.transaction.annotation.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.module.Item;
import ru.practicum.shareit.item.repository.ItemStorage;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.mapper.RequestMapper;
import ru.practicum.shareit.request.module.Request;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.module.User;
import ru.practicum.shareit.user.repository.UserStorage;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class RequestServicelmpl implements RequestService {

     private final RequestRepository requestRepository;

     private final UserStorage userStorage;

     private final ItemStorage itemStorage;

    @Transactional
    @Override
    public ItemRequestDto create(Long userId, RequestDto requestDto) {
       if (userId == null) {
           throw new IllegalArgumentException("X-Sharer-User-Id не передан");
       }
       Request request = RequestMapper.mapToRequest(userId, requestDto);
       Request savedRequest = requestRepository.save(request);
        List<Item> items = itemStorage.findByOwnerId(savedRequest.getId());
        return RequestMapper.toItemRequestDto(savedRequest, items);
    }

    @Override
    public List<ItemRequestDto> findRequest(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("X-Sharer-User-Id не передан");
        }
        User user = userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("User id = " + userId + " не найден"));
        List<Request> requests = requestRepository.findByUserId(userId);
        return requests.stream()
                .map(request -> {
                    List<Item> items = itemStorage.findByRequest_Id(request.getId());
                    return RequestMapper.toItemRequestDto(request, items);
                })
                .toList();
    }

    @Override
    public ItemRequestDto findById(Long userId, Long id) {
        if (userId == null) {
            throw new IllegalArgumentException("X-Sharer-User-Id не передан");
        }
        Request request = requestRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Запрос по id = " + id + " не найден"));
        List<Item> items = itemStorage.findByRequest_Id(id);
        System.out.println("Items for request " + id + ": " + items);
        return RequestMapper.toItemRequestDto(request, items);
    }
}
