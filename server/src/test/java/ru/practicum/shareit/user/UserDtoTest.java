package ru.practicum.shareit.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.user.dto.UserDto;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.practicum.shareit.model.TestModels.userDto;

@JsonTest
public class UserDtoTest {

    @Autowired
    private JacksonTester<UserDto> jacksonTester;

    @Test
    @DisplayName("Конвертация пользователя в DTO")
    public void itemRequestDtoTest() throws IOException {
        JsonContent<UserDto> content = jacksonTester.write(userDto);

        assertThat(content).hasJsonPath("$.id");
        assertThat(content).hasJsonPath("$.name");
        assertThat(content).hasJsonPath("$.email");
    }

}