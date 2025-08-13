package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Repository
public class UserStorage {

    private final Map<Long, User> users = new HashMap<>();

    private final Set<String> emails = new HashSet<>();

    public Set<String> getEmails() {
        return emails;
    }

    public User create (User user) {
        user.setId(getNextId());
        emails.add(user.getEmail());
        users.put(user.getId(), user);
        return user;
    }

    public User getById(Long userId) {
        return users.get(userId);
    }

    public User update (User user) {
        return users.put(user.getId(), user);
    }

    public User delete (Long userId) {
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
        return ++currentMaxId+1;
    }
}