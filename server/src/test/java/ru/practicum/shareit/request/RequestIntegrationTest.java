package ru.practicum.shareit.request;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.repository.ItemStorage;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.module.Request;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.request.server.RequestService;
import ru.practicum.shareit.user.module.User;
import ru.practicum.shareit.user.repository.UserStorage;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class RequestIntegrationTest {

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private RequestService requestService;

    @Autowired
    private ItemStorage itemStorage;

    @Autowired
    private UserStorage userStorage;

    @Test
   void findById() {
        User user = new User();
        user.setName("Иван");
        user.setEmail("ivan@example.com");
        User savedUser = userStorage.save(user);
        Request request = new Request();
        request.setDescription("Нужен чайник");
        request.setUserId(savedUser.getId());
        request.setCreated(LocalDateTime.now());
        Request saved = requestRepository.save(request);
        Optional<Request> found = requestRepository.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(saved.getId());
        assertThat(found.get().getDescription()).isEqualTo("Нужен чайник");
    }

    @Test
    void createRequest() {
        User user = new User();
        user.setName("Мария");
        user.setEmail("maria@example.com");
        User savedUser = userStorage.save(user);

        RequestDto requestDto = new RequestDto();
        requestDto.setDescription("Нужен фен");

        ItemRequestDto created = requestService.create(savedUser.getId(), requestDto);

        assertThat(created).isNotNull();
        assertThat(created.getId()).isNotNull();
        assertThat(created.getDescription()).isEqualTo("Нужен фен");
    }

    @Test
    void findRequestByUser() {
        User user = new User();
        user.setName("Олег");
        user.setEmail("oleg@example.com");
        User savedUser = userStorage.save(user);

        Request request1 = new Request();
        request1.setUserId(savedUser.getId());
        request1.setDescription("Нужна плита");
        request1.setCreated(LocalDateTime.now());
        requestRepository.save(request1);

        Request request2 = new Request();
        request2.setUserId(savedUser.getId());
        request2.setDescription("Нужен холодильник");
        request2.setCreated(LocalDateTime.now());
        requestRepository.save(request2);
        var requests = requestService.findRequest(savedUser.getId());

        assertEquals(2, requests.size());
        List<String> descriptions = requests.stream().map(r -> r.getDescription()).toList();
        assertTrue(descriptions.contains("Нужна плита"));
        assertTrue(descriptions.contains("Нужен холодильник"));
    }

    @Test
    void findRequestById() {
        User user = new User();
        user.setName("Сергей");
        user.setEmail("sergey@example.com");
        User savedUser = userStorage.save(user);

        Request request = new Request();
        request.setUserId(savedUser.getId());
        request.setDescription("Нужен чайник");
        request.setCreated(LocalDateTime.now());
        Request savedRequest = requestRepository.save(request);
        ItemRequestDto dto = requestService.findById(savedUser.getId(), savedRequest.getId());

        assertNotNull(dto);
        assertEquals(savedRequest.getId(), dto.getId());
        assertEquals("Нужен чайник", dto.getDescription());
    }

    @Test
    void createRequest_noUserId_shouldThrow() {
        RequestDto requestDto = new RequestDto();
        requestDto.setDescription("Тест");
        assertThrows(IllegalArgumentException.class,
                () -> requestService.create(null, requestDto));
    }

    @Test
    void findById_requestNotFound_shouldThrow() {
        User user = new User();
        user.setName("Test");
        user.setEmail("test@example.com");
        User saved = userStorage.save(user);

        assertThrows(NotFoundException.class,
                () -> requestService.findById(saved.getId(), 999L));
    }

    @Test
    void findById_noUserId_shouldThrow() {
        assertThrows(IllegalArgumentException.class,
                () -> requestService.findById(null, 1L));
    }

    @Test
    void findRequest_noUserId_shouldThrow() {
        assertThrows(IllegalArgumentException.class,
                () -> requestService.findRequest(null));
    }
}

