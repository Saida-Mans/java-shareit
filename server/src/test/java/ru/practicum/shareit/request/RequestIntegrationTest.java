package ru.practicum.shareit.request;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.request.module.Request;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.module.User;
import ru.practicum.shareit.user.repository.UserStorage;
import java.time.LocalDateTime;
import java.util.Optional;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Transactional
public class RequestIntegrationTest {

    @Autowired
    private RequestRepository requestRepository;

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
}

