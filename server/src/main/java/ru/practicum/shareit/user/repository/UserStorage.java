package ru.practicum.shareit.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.user.module.User;

public interface UserStorage extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);
}
