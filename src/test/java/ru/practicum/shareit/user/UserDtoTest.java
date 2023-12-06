package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.user.dto.UserDto;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
@JsonTest
public class UserDtoTest {

        @Autowired
        private JacksonTester<UserDto> jacksonTester;

        @Test
        public void itemRequestDtoTest() throws IOException {

            UserDto userDto1 = UserDto.builder()
                    .id(2L)
                    .name("name1")
                    .email("email@mail.ru")
                    .build();

            JsonContent<UserDto> content = jacksonTester.write(userDto1);

            assertThat(content).hasJsonPath("$.id");
            assertThat(content).hasJsonPath("$.name");
            assertThat(content).hasJsonPath("$.email");
        }
    }