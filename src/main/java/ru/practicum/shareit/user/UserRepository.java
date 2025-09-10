package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;

import java.util.*;

@Deprecated

@Repository
public class UserRepository  {

    private final Map<Long, User> users = new HashMap<>();

    private final Set<String> emails = new HashSet<>();

    public Set<String> getEmails() {
        return new HashSet<>(emails);
    }

    public User create(User user) {
        if (emails.contains(user.getEmail())) {
            throw new IllegalArgumentException("Email уже зарегистрирован");
        }
        user.setId(getNextId());
        users.put(user.getId(), user);
        emails.add(user.getEmail());
        return user;
    }

    public User getById(Long userId) {
        return users.get(userId);
    }

    public User update(User user) {
        return users.put(user.getId(), user);
    }

    public User delete(Long userId) {
        User user = users.get(userId);
        emails.remove(user.getEmail());
        users.remove(userId, user);
        return user;
    }

    private Long getNextId() {
        Long currentMaxId = users.keySet()
                .stream()
                .max(Long::compareTo)
                .orElse(0L);
        return ++currentMaxId + 1;
    }
}