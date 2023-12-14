package ru.practicum.shareit.user;

import org.junit.jupiter.api.Assertions;
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
import java.util.stream.Collectors;

@Transactional
@SpringBootTest
@AutoConfigureTestDatabase
public class UserServiceIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private UserMapper userMapper;

    @Test
    public void getUsersIntegrationTest() {

        User user = new User(0L, "userName", "user@user.ru");
        User userNew = userRepository.save(user);
        UserDto userDto = userMapper.toDto(userNew);

        System.out.println("userNew.getId()**************************" + userNew.getId());

        List<UserDto> allusers = userRepository.findAll()
                .stream()
                .map(x -> userMapper.toDto(x))
                .collect(Collectors.toList());

        Assertions.assertEquals(userDto.getId(), allusers.get(0).getId());
        Assertions.assertEquals(userDto.getName(), allusers.get(0).getName());
        Assertions.assertEquals(userDto.getEmail(), allusers.get(0).getEmail());
    }
}