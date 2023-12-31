package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    private final UserMapper userMapper;

    @PostMapping
    public UserDto create(@Valid @RequestBody UserDto userDto) {
        log.info("Create new user");
        return userService.create(userMapper.toUser(userDto));
    }

    @PatchMapping("/{userId}")
    public UserDto update(@Valid @RequestBody User user, @PathVariable long userId) {
        log.info("Update user");
        return userService.update(user, userId);
    }

    @GetMapping
    @ResponseBody
    public List<UserDto> getAll() {
        log.info("Get all users");
        return userService.getAll();
    }

    @GetMapping("/{userId}")
    public UserDto getById(@PathVariable long userId) {
        log.info("Get user by id=" + userId);
        return userMapper.toDto(userService.getById(userId));
    }

    @DeleteMapping("/{userId}")
    public void deleteById(@PathVariable long userId) {
        log.info("Delete user with id=" + userId);
        userService.deleteById(userId);
    }
}