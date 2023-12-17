package ru.practicum.shareit.booking;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.model.JsonModels.*;
import static ru.practicum.shareit.model.TestModels.item;
import static ru.practicum.shareit.model.TestModels.last;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class BookingControllerTest {

    private final String url = "/bookings";

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BookingService bookingService;

    @Test
    @DisplayName("Создание бронирования")
    public void createTest() throws Exception {
        when(bookingService.create(any(), anyInt())).thenReturn(BookingMapper.toBookingDto(last));

        String bookingJson = createBookingJson(item.getId(), start, end);

        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookingJson)
                        .header("X-Sharer-User-Id", 1))
                .andDo(print())
                .andExpect(status().isOk())

                .andExpect(jsonPath("$.id").value(last.getId()))
                .andExpect(jsonPath("$.item.name").value(last.getItem().getName()))
                .andExpect(jsonPath("$.booker.id").value(last.getBooker().getId()))
                .andExpect(jsonPath("$.status").value(statusApr));
    }

    @Test
    @DisplayName("Подтверждение бронирования")
    public void approveTest() throws Exception {
        when(bookingService.approveBooking(anyInt(), anyInt(), anyBoolean())).thenReturn(BookingMapper.toBookingDto(last));

        mockMvc.perform(patch(url + "/{bookingId}", last.getId())
                        .header("X-Sharer-User-Id", 1)
                        .param("approved", String.valueOf(true)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(statusApr));
    }

    @Test
    @DisplayName("Получение бронирования")
    public void getBookingTest() throws Exception {
        when(bookingService.get(anyInt(), anyInt())).thenReturn(BookingMapper.toBookingDto(last));

        mockMvc.perform(get(url + "/{bookingId}", last.getId())
                        .header("X-Sharer-User-Id", 1))
                .andDo(print())
                .andExpect(status().isOk())

                .andExpect(jsonPath("$.id").value(last.getId()))
                .andExpect(jsonPath("$.item.name").value(last.getItem().getName()))
                .andExpect(jsonPath("$.booker.id").value(last.getBooker().getId()))
                .andExpect(jsonPath("$.status").value(statusApr));
    }

    @Test
    @DisplayName("Получение бронирования по id пользователя")
    public void getUserBookingTest() throws Exception {
        when(bookingService.getByUserId(anyInt(), anyInt(), anyInt(), any())).thenReturn(BookingMapper.toDtoList(List.of(last)));

        mockMvc.perform(get(url)
                        .header("X-Sharer-User-Id", 1))
                .andDo(print())
                .andExpect(status().isOk())

                .andExpect(jsonPath("$[0].id").value(last.getId()))
                .andExpect(jsonPath("$[0].item.name").value(last.getItem().getName()))
                .andExpect(jsonPath("$[0].booker.id").value(last.getBooker().getId()))
                .andExpect(jsonPath("$[0].status").value(statusApr));
    }

    @Test
    @DisplayName("Получение бронирования владельцем")
    public void getOwnerBookingTest() throws Exception {
        when(bookingService.getByOwnerId(anyInt(), anyInt(), anyInt(), any())).thenReturn(BookingMapper.toDtoList(List.of(last)));

        mockMvc.perform(get(url + "/owner")
                        .header("X-Sharer-User-Id", 1))
                .andDo(print())
                .andExpect(status().isOk())

                .andExpect(jsonPath("$[0].id").value(last.getId()))
                .andExpect(jsonPath("$[0].item.name").value(last.getItem().getName()))
                .andExpect(jsonPath("$[0].booker.id").value(last.getBooker().getId()))
                .andExpect(jsonPath("$[0].status").value(statusApr));
    }
}