package ru.practicum.shareit.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.ComponentScan;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.when;
import static ru.practicum.shareit.model.JsonModels.email;
import static ru.practicum.shareit.model.JsonModels.userName;
import static ru.practicum.shareit.model.TestModels.user;
import static ru.practicum.shareit.model.TestModels.userDto;

@ExtendWith(MockitoExtension.class)
@ComponentScan(basePackages = {"ru.yandex.practicum.shareit"})
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void getUsersTest() {
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<UserDto> lists = userService.getAll();

        assertEquals(lists.size(), 1);
        assertEquals(lists.get(0).getName(), userName);
        assertEquals(lists.get(0).getEmail(), email);
    }

    @Test
    @DisplayName("Создание пользователя")
    void addUserTest() {
        when(userRepository.save(Mockito.any(User.class))).thenReturn(user);

        UserDto userDto1 = userService.create(userDto);

        assertEquals(userDto1.getName(), userName);
        assertEquals(userDto1.getEmail(), email);
    }

    @Test
    @DisplayName("Получение пользователя по id")
    void getUserTest() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));

        UserDto userDto1 = userService.getById(user.getId());

        assertEquals(userDto1.getName(), userName);
        assertEquals(userDto1.getEmail(), email);
    }

    @Test
    @DisplayName("Получение пользователя с неверным id")
    void getUserWithWrongIdTest() {

        UserNotFoundException userNotFoundException = assertThrows(UserNotFoundException.class,
                () -> userService.getById(2));

        assertNotNull(userNotFoundException.getMessage());
    }

    @Test
    @DisplayName("Обновление пользователя")
    void updateUserTest() {
        when(userRepository.getReferenceById(anyInt())).thenReturn(user);

        UserDto userDto1 = new UserDto(1, "updatedName", "updated@yandex.ru");
        user.setName("updatedName");
        user.setEmail("updated@yandex.ru");
        when(userRepository.save(Mockito.any(User.class))).thenReturn(user);
        UserDto updatedUser = userService.update(userDto1, user.getId());

        assertEquals(updatedUser.getName(), "updatedName");

        user.setName(userName);
        user.setEmail(email);
    }

    @Test
    @DisplayName("Удаление пользователя")
    void deleteUserTest() {
        assertDoesNotThrow(() -> userService.delete(user.getId()));
    }
}