package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.Month;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class BookingDtoTest {

    @Autowired
    private JacksonTester<Booking> jacksonTester;

    @Test
    public void toBookingTest() throws IOException {
        BookingMapper bookingMapper = new BookingMapper();

        User user = new User(1L, "name", "mail@mail.ru");
        Item item = new Item();
        item.setId(1L);
        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(1L);
        bookingDto.setItemId(1L);
        bookingDto.setStart(LocalDateTime.of(2023, Month.APRIL, 8, 12, 30));
        bookingDto.setEnd(LocalDateTime.of(2023, Month.APRIL, 10, 12, 30));
        bookingDto.setStatus(BookingStatus.APPROVED);

        Booking booking = bookingMapper.toBooking(bookingDto, user, item);

        JsonContent<Booking> content = jacksonTester.write(booking);

        assertThat(content).hasJsonPath("$.id");
        assertThat(content).hasJsonPath("$.start");
        assertThat(content).hasJsonPath("$.end");
        assertThat(content).hasJsonPath("$.status");
        assertThat(content).hasJsonPath("$.booker");
        assertThat(content).hasJsonPath("$.item");
    }
}