package ru.practicum.shareit.user;

import java.util.List;

public interface UserService {
    public User create(User user);

    public User update(User user, long userId);

    public List<User> getAll();

    public User getById(long userId);

    public void deleteById(long id);
}
