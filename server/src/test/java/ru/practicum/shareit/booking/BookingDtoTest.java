package ru.practicum.shareit.booking;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.practicum.shareit.model.TestModels.last;

@JsonTest
public class BookingDtoTest {
    @Autowired
    private JacksonTester<BookingDto> jacksonTester;

    @Test
    @DisplayName("Конвертация бронирования в DTO")
    public void bookingToDtoTest() throws IOException {
        BookingDto bookingDto = BookingMapper.toBookingDto(last);

        JsonContent<BookingDto> content = jacksonTester.write(bookingDto);

        assertThat(content).hasJsonPath("$.id");
        assertThat(content).hasJsonPath("$.start");
        assertThat(content).hasJsonPath("$.end");
        assertThat(content).hasJsonPath("$.status");
        assertThat(content).hasJsonPath("$.booker");
        assertThat(content).hasJsonPath("$.item");

    }
}