package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByIdTest() {
        User user = new User(0L, "userName", "email@mail.ru");
        userRepository.save(user);
        User check = userRepository.findById(user.getId()).orElseThrow();
        assertNotNull(check);
        assertEquals(check.getId(), user.getId());
        assertEquals(check.getName(), "userName");
    }
}