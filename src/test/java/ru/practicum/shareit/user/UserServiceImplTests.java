package ru.practicum.shareit.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTests {

    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;

    public static User inputUser = User.builder()
            .id(0L)
            .name("Николай")
            .email("nik@mail.ru")
            .build();

    public static User inputUserAfterSave = User.builder()
            .id(1L)
            .name("Николай")
            .email("nik@mail.ru")
            .build();

    public static UserDto outputUserDto = UserDto.builder()
            .id(1L)
            .name("Николай")
            .email("nik@mail.ru")
            .build();

    public static User inputUserWithOtherName = User.builder()
            .id(0L)
            .name("Иван")
            .email("nik@mail.ru")
            .build();
    public static User UserWithOtherNameAfterSave = User.builder()
            .id(1L)
            .name("Иван")
            .email("nik@mail.ru")
            .build();

    public static UserDto UserWithOtherNameDto = UserDto.builder()
            .id(1L)
            .name("Иван")
            .email("nik@mail.ru")
            .build();

    public static List<User> users = new ArrayList<>();

    @Test
    public void createUserTest() {

        Mockito.when(userRepository.save(inputUser)).thenReturn(inputUserAfterSave);
        Mockito.when(userMapper.toDto(inputUserAfterSave)).thenReturn(outputUserDto);

        UserDto result = userService.create(inputUser);

        Assertions.assertEquals(outputUserDto, result);
    }

    @Test
    public void updateUserTest() {

        Mockito.when(userRepository.existsById(1L)).thenReturn(true);
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(inputUserAfterSave));

        Mockito.when(userRepository.save(inputUserAfterSave)).thenReturn(UserWithOtherNameAfterSave);
        Mockito.when(userMapper.toDto(UserWithOtherNameAfterSave)).thenReturn(UserWithOtherNameDto);

        UserDto result = userService.update(inputUserWithOtherName, 1L);

        Assertions.assertEquals(UserWithOtherNameDto.getName(), result.getName());
    }

    @Test
    public void getAllUsersTest() {
        users.add(inputUserAfterSave);
        Mockito.when(userRepository.findAll()).thenReturn(users);
        List<UserDto> usersDto = users.stream()
                .map(x -> userMapper.toDto(x))
                .collect(Collectors.toList());

        List<UserDto> result = userService.getAll();

        Assertions.assertEquals(usersDto, result);
    }

    @Test
    public void getByIdTest() {
        Mockito.when(userRepository.existsById(1L)).thenReturn(true);

        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(inputUserAfterSave));

        User result = userService.getById(1L);

        Assertions.assertEquals(inputUserAfterSave, result);
    }

    @Test
    public void deleteByIdTest() {
        Mockito.when(userRepository.existsById(1L)).thenReturn(true);
        assertDoesNotThrow(() -> userService.deleteById(1L));
    }
}