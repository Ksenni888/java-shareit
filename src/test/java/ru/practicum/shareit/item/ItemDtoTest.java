package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.Month;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class ItemDtoTest {

    @Autowired
    private JacksonTester<ItemDto> jacksonItemTester;
    @Autowired
    private JacksonTester<CommentDto> jacksonCommentTester1;

    @Test
    public void itemDtoJsonTest() throws IOException {

        ItemDto itemDto = ItemDto.builder()
                .id(1L)
                .name("name")
                .description("description")
                .available(true)
                .requestId(1L)
                .build();


        JsonContent<ItemDto> result = jacksonItemTester.write(itemDto);

        assertThat(result).hasJsonPath("$.id");
        assertThat(result).hasJsonPath("$.name");
        assertThat(result).hasJsonPath("$.description");
        assertThat(result).hasJsonPath("$.available");
        assertThat(result).hasJsonPath("$.requestId");
    }

    @Test
    public void itemCommentDtoTest() throws IOException {
        CommentDto commentDto = CommentDto.builder()
                .id(1L)
                .text("text")
                .author(1L)
                .authorName("name")
                .created(LocalDateTime.of(2023, Month.APRIL, 8, 12, 30))
                .build();

        JsonContent<CommentDto> content = jacksonCommentTester1.write(commentDto);

        assertThat(content).hasJsonPath("$.id");
        assertThat(content).hasJsonPath("$.text");
        assertThat(content).hasJsonPath("$.authorName");
        assertThat(content).hasJsonPath("$.created");
    }
}