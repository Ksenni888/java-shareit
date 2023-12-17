package ru.practicum.shareit.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.List;

import static ru.practicum.shareit.model.TestModels.user;

@Transactional
@SpringBootTest
@AutoConfigureTestDatabase
public class UserIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserServiceImpl userService;

    @Test
    @DisplayName("Получение всех пользователей")
    public void getUsersIntegrationTest() {
        User userNew = userRepository.save(user);
        List<UserDto> list = userService.getAll();

        Assertions.assertEquals(list.size(), 1);
        Assertions.assertEquals(UserMapper.toUserDto(userNew), list.get(0));
    }

}