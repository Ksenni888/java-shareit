package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private final Map<Long, User> users = new HashMap<>();

    private long id;

    @Override
    public User create(User user) {
        user.setId(incrementId());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user, long userId) {
        users.put(userId, user);
        return user;
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getById(long userId) {
        return users.get(userId);
    }

    @Override
    public void deleteById(long userId) {
        users.remove(userId);
    }

    @Override
    public boolean containsUser(long userId) {
        return users.containsKey(userId);
    }

    public long incrementId() {
        return ++id;
    }

    public boolean existsByEmail(String email) {
      return users.values().stream()
              .anyMatch(x -> x.getEmail().equals(email));
    }
}
