package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.request.dto.ItemDtoForRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class RequestDtoTest {

    @Autowired
    private JacksonTester<ItemRequestDto> jacksonTester;

    @Test
    public void itemRequestDtoTest() throws IOException {

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(1L);

        ItemDtoForRequest itemDtoForRequest = ItemDtoForRequest.builder()
                .id(1L)
                .requestId(1L)
                .build();

        List<ItemDtoForRequest> items = List.of(itemDtoForRequest);

        ItemRequestDto itemRequestDto = ItemRequestDto.builder()
                .id(1L)
                .created(LocalDateTime.of(2023, Month.APRIL, 8, 12, 30))
                .description("description")
                .items(items)
                .build();

        JsonContent<ItemRequestDto> content = jacksonTester.write(itemRequestDto);

        assertThat(content).hasJsonPath("$.id");
        assertThat(content).hasJsonPath("$.description");
        assertThat(content).hasJsonPath("$.created");
        assertThat(content).hasJsonPath("$.items");
    }
}