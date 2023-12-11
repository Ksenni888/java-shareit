package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.booking.controller.BookingController;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class BookingControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    private MockMvc mockMvc;
    @Mock
    private BookingServiceImpl bookingService;
    @InjectMocks
    private BookingController bookingController;

    @BeforeEach
    void setMockMvc() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(bookingController)
                .build();
    }

    User user = new User(1L, "userName", "email@mail.ru");
    User user2 = new User(2L, "userName2", "email2@mail.ru");
    Item item = new Item(1L, "itemName", "itemDescription", true, user, null);
    LocalDateTime start = LocalDateTime.of(2024, Month.APRIL, 8, 12, 30);
    LocalDateTime end = LocalDateTime.of(2024, Month.APRIL, 12, 12, 30);
    Booking booking = new Booking(1L, start, end, item, user, BookingStatus.APPROVED);
    String addBooking = createBookingDtoJson(1L, start, end, BookingStatus.APPROVED);

    @Test
    public void createBookingTest() throws Exception {
        Mockito.when(bookingService.createBooking(anyLong(), any())).thenReturn(booking);

        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", booking.getBooker().getId())
                        .content(addBooking)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.start[0]").value(2024))
                .andExpect(jsonPath("$.start[1]").value(4));
    }

    @Test
    public void checkRequestTest() throws Exception {
        Mockito.when(bookingService.checkRequest(anyLong(), anyLong(), anyString())).thenReturn(booking);

        mockMvc.perform(patch("/bookings/{bookingId}", booking.getId())
                        .header("X-Sharer-User-Id", item.getOwner().getId())
                        .param("approved", String.valueOf(true))
                        .content(addBooking)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.status").value("APPROVED"))
                .andExpect(jsonPath("$.start[0]").value(2024))
                .andExpect(jsonPath("$.start[1]").value(4));
    }

    @Test
    public void getBookingTest() throws Exception {
        Mockito.when(bookingService.getBooking(anyLong(), anyLong())).thenReturn(booking);

        mockMvc.perform(get("/bookings/{bookingId}", booking.getId())
                        .header("X-Sharer-User-Id", booking.getBooker().getId())
                        .content(addBooking)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.start[0]").value(2024))
                .andExpect(jsonPath("$.start[1]").value(4));
    }

    @Test
    public void getBookingsByStatusTest() throws Exception {
        Mockito.when(bookingService.getBookingsByStatus(booking.getBooker().getId(), "FUTURE",
                PageRequest.of(0, 10, Sort.by("start").descending()))).thenReturn(List.of(booking));

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", booking.getBooker().getId())
                        .param("state", "FUTURE")
                        .content(List.of(addBooking).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$.[0]id").value(1L))
                .andExpect(jsonPath("$.[0]status").value("APPROVED"))
                .andExpect(jsonPath("$.[0]start[0]").value(2024))
                .andExpect(jsonPath("$.[0]start[1]").value(4));
    }

    @Test
    public void getUserBookings() throws Exception {
        Mockito.when(bookingService.getUserBookings(booking.getBooker().getId(), "FUTURE",
                PageRequest.of(0, 10, Sort.by("start").descending()))).thenReturn(List.of(booking));
        LocalDateTime start = LocalDateTime.of(2024, Month.APRIL, 8, 12, 30);
        LocalDateTime end = LocalDateTime.of(2024, Month.APRIL, 12, 12, 30);
        String addBooking1 = createBookingDtoJson(2L, start, end, BookingStatus.APPROVED);

        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", user.getId())
                        .param("state", "FUTURE")
                        .content(List.of(addBooking1).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$.[0]id").value(1L))
                .andExpect(jsonPath("$.[0]start[1]").value(4));

    }

    public static String createBookingDtoJson(long id, LocalDateTime start, LocalDateTime end, BookingStatus status) {
        return "{\n" +
                "    \"id\": \"" + id + "\",\n" +
                "    \"status\": \"" + status + "\",\n" +
                "    \"start\": \"" + start + "\",\n" +
                "    \"end\": \"" + end + "\"\n" +
                "}";
    }
}