package ru.practicum.shareit.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static ru.practicum.shareit.model.TestModels.*;
import static ru.practicum.shareit.model.JsonModels.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Получение пользователя по его email")
    void findByEmailTest() {
        userRepository.save(user);
        Optional<User> check = userRepository.findByEmail(email);
        assertNotNull(check);
        assertEquals(check.get().getEmail(), email);
        assertEquals(check.get().getName(), userName);
    }
}