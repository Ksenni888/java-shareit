package ru.practicum.shareit.request;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.practicum.shareit.model.TestModels.itemRequestDto;

@JsonTest
public class ItemRequestDtoTest {

    @Autowired
    private JacksonTester<ItemRequestDto> jacksonTester;

    @Test
    @DisplayName("Конвертация запроса в DTO")
    public void itemRequestDtoTest() throws IOException {
        JsonContent<ItemRequestDto> content = jacksonTester.write(itemRequestDto);

        assertThat(content).hasJsonPath("$.id");
        assertThat(content).hasJsonPath("$.description");
        assertThat(content).hasJsonPath("$.requesterId");
        assertThat(content).hasJsonPath("$.created");
        assertThat(content).hasJsonPath("$.items");
    }
}