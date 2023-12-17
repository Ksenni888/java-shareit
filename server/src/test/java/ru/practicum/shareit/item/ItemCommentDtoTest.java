package ru.practicum.shareit.item;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.practicum.shareit.model.TestModels.commentDto;
import static ru.practicum.shareit.model.TestModels.itemDto;


@JsonTest
public class ItemCommentDtoTest {

    @Autowired
    private JacksonTester<ItemDto> jacksonItemTester;
    @Autowired
    private JacksonTester<CommentDto> jacksonCommentTester1;

    @Test
    @DisplayName("Конвертация вещи в DTO")
    public void itemDtoJsonTest() throws IOException {
        JsonContent<ItemDto> result = jacksonItemTester.write(itemDto);

        assertThat(result).hasJsonPath("$.id");
        assertThat(result).hasJsonPath("$.name");
        assertThat(result).hasJsonPath("$.description");
        assertThat(result).hasJsonPath("$.available");
        assertThat(result).hasJsonPath("$.requestId");
    }

    @Test
    @DisplayName("Конвертация комментария в DTO")
    public void itemCommentDtoTest() throws IOException {
        JsonContent<CommentDto> content = jacksonCommentTester1.write(commentDto);

        assertThat(content).hasJsonPath("$.id");
        assertThat(content).hasJsonPath("$.text");
        assertThat(content).hasJsonPath("$.authorName");
        assertThat(content).hasJsonPath("$.created");
    }
}