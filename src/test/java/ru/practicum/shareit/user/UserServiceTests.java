package ru.practicum.shareit.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exeption.ExistExeption;
import ru.practicum.shareit.exeption.ObjectNotFoundException;
import ru.practicum.shareit.exeption.ValidException;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;

    public User inputUser = User.builder()
            .id(0L)
            .name("Николай")
            .email("nik@mail.ru")
            .build();

    public User inputUserAfterSave = User.builder()
            .id(1L)
            .name("Николай")
            .email("nik@mail.ru")
            .build();

    public UserDto outputUserDto = UserDto.builder()
            .id(1L)
            .name("Николай")
            .email("nik@mail.ru")
            .build();

    public User userNewName = User.builder()
            .id(0L)
            .name("Иван")
            .email("nik@mail.ru")
            .build();

    public User userNewNameSave = User.builder()
            .id(1L)
            .name("Иван")
            .email("nik@mail.ru")
            .build();

    public UserDto userWithOtherNameDto = UserDto.builder()
            .id(1L)
            .name("Иван")
            .email("nik@mail.ru")
            .build();

    public List<User> users = new ArrayList<>();

    @Test
    public void createUserTest() {

        Mockito.when(userRepository.save(inputUser)).thenReturn(inputUserAfterSave);
        Mockito.when(userMapper.toDto(inputUserAfterSave)).thenReturn(outputUserDto);

        UserDto result = userService.create(inputUser);

        Assertions.assertEquals(outputUserDto, result);
    }

    @Test
    public void createUserWithIdTest() {
       ValidException exception = Assertions.assertThrows(
                ValidException.class,
                () -> userService.create(inputUserAfterSave));
        assertNotNull(exception.getMessage());
    }

    @Test
    public void createUserWithSameEmailTest() {
        Mockito.when(userRepository.save(inputUser)).thenThrow(new ExistExeption("Email can't be the same"));
        ExistExeption exception = Assertions.assertThrows(
                ExistExeption.class,
                () -> userService.create(inputUser));
        assertNotNull(exception.getMessage());
    }

    @Test
    public void updateUserTest() {

        Mockito.when(userRepository.existsById(1L)).thenReturn(true);
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(inputUserAfterSave));

        Mockito.when(userRepository.save(inputUserAfterSave)).thenReturn(userNewNameSave);
        Mockito.when(userMapper.toDto(userNewNameSave)).thenReturn(userWithOtherNameDto);

        UserDto result = userService.update(userNewName, 1L);

        Assertions.assertEquals(userWithOtherNameDto.getName(), result.getName());
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

    @Test
    public void checkUserExistsTest() {
        Mockito.when(userRepository.existsById(inputUserAfterSave.getId())).thenReturn(false);
        ObjectNotFoundException exception = Assertions.assertThrows(
                ObjectNotFoundException.class,
                () -> userService.checkUserExists(inputUserAfterSave.getId()));
        assertNotNull(exception.getMessage());
    }
}