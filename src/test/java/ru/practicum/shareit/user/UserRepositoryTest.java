package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
@DataJpaTest
public class UserRepositoryTest {

        @Autowired
        private UserRepository userRepository;

        @Test
        void getByEmailTest() {
            User user = User.builder()
                    .id(0L)
                    .name("Николай")
                    .email("nik@mail.ru")
                    .build();

            userRepository.save(user);
            Optional<User> check = userRepository.findById(1L);
            assertNotNull(check);
            assertEquals(check.get().getId(), 1L);
            assertEquals(check.get().getName(), "Николай");
        }
    }