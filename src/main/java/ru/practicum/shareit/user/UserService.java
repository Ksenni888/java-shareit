package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    public UserDto create(User user);

    public UserDto update(User user, long userId);

    public List<UserDto> getAll();

    public UserDto getById(long userId);

    public void deleteById(long id);
}
