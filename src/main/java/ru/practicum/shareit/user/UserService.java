package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
   UserDto create(User user);

    UserDto update(User user, long userId);

    List<UserDto> getAll();

    User getById(long userId);

    void deleteById(long id);
}
