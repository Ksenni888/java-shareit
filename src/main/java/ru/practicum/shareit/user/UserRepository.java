package ru.practicum.shareit.user;

import java.util.List;

public interface UserRepository {
    public User create(User user);

    public User update(User user, long userId);

    public List<User> getAll();

    public User getById(long userId);

    public void deleteById(long userId);

    public boolean containsUser (long userId);
}
