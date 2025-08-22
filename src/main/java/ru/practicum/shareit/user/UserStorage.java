package ru.practicum.shareit.user;

import java.util.Set;

public interface UserStorage {

    User create(User user);

    User getById(Long userId);

    User update(User user);

    Set<String> getEmails();

    User delete(Long userId);
}
