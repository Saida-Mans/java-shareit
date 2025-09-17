package ru.practicum.shareit.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.request.module.Request;
import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findByUserId(Long userId);

    Optional<Request> findById(Long id);
}
