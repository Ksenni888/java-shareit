package ru.practicum.shareit.booking;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.exception.BookingNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static ru.practicum.shareit.model.JsonModels.*;
import static ru.practicum.shareit.model.TestModels.*;

@Transactional
@ExtendWith(MockitoExtension.class)
@ComponentScan(basePackages = {"ru.yandex.practicum.shareit"})
class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private UserService userService;
    @Mock
    private ItemService itemService;

    @InjectMocks
    private BookingServiceImpl bookingService;

    @Test
    @DisplayName("Получение бронирования")
    void getBookingTest() {
        when(bookingRepository.findById(anyInt())).thenReturn(Optional.of(next));

        Booking booking = BookingMapper.fromBookingDto(bookingService.get(next.getId(), user.getId()), item, booker);
        assertEquals(booking.getBooker(), next.getBooker());
        assertEquals(booking.getItem(), next.getItem());
        assertEquals(booking.getStart(), next.getStart());
    }

    @Test
    @DisplayName("Создание бронирования")
    void createBookingTest() {
        when(bookingRepository.save(any())).thenReturn(next);
        when(userService.getById(anyInt())).thenReturn(userDto);
        when(itemService.getById(anyInt())).thenReturn(item);

        BookingDto bookingDto1 = bookingService.create(bookingDto, booker.getId());

        assertEquals(bookingDto1.getItem().getId(), item.getId());
        assertEquals(bookingDto1.getStart(), start);
        assertEquals(bookingDto1.getEnd(), end);
        assertEquals(bookingDto1.getStatus(), Status.WAITING);
    }

    @Test
    @DisplayName("Подтверждение бронирования с неверным статусом")
    void approveBookingWrongStatusTest() {
        when(bookingRepository.getReferenceById(anyInt())).thenReturn(next);

        ValidationException validationException = assertThrows(ValidationException.class,
                () -> bookingService.approveBooking(next.getId(), user.getId(), available));

        assertNotNull(validationException.getMessage());
    }

    @Test
    @DisplayName("Подтверждение бронирования с неверным владельцем")
    void approveBookingWrongOwnerTest() {
        when(bookingRepository.getReferenceById(anyInt())).thenReturn(next);

        next.setStatus(Status.WAITING);
        BookingNotFoundException notFoundException = assertThrows(BookingNotFoundException.class,
                () -> bookingService.approveBooking(next.getId(), booker.getId(), available));
        next.setStatus(Status.APPROVED);

        assertNotNull(notFoundException.getMessage());

    }

    @Test
    @DisplayName("Подтверждение бронирования")
    void approveBookingTest() {
        when(bookingRepository.getReferenceById(anyInt())).thenReturn(next);
        when(bookingRepository.save(any())).thenReturn(next);

        next.setStatus(Status.WAITING);

        BookingDto bookingDto1 = bookingService.approveBooking(next.getId(), user.getId(), available);

        assertEquals(bookingDto1.getId(), next.getId());
        assertEquals(bookingDto1.getStart(), next.getStart());
        assertEquals(bookingDto1.getEnd(), next.getEnd());
        assertEquals(bookingDto1.getStatus(), next.getStatus());
        assertEquals(bookingDto1.getItemId(), next.getItem().getId());
    }

    @Test
    @DisplayName("Получение бронирований пользователя")
    void getUserBookingsTest() {
        when(userService.getById(anyInt())).thenReturn(UserMapper.toUserDto(booker));
        when(bookingRepository.findAllByBookerIdOrderByStartDesc(anyInt(), any())).thenReturn(List.of(last, next));

        List<BookingDto> list = bookingService.getByUserId(0, 20, booker.getId(), "ALL");

        assertEquals(list.size(), 2);

    }

    @Test
    @DisplayName("Получение бронирований владельца")
    void getOwnerBookingsTest() {
        when(userService.getById(anyInt())).thenReturn(UserMapper.toUserDto(booker));
        when(bookingRepository.findAllByItemOwnerIdOrderByStartDesc(anyInt(), any())).thenReturn(List.of(last, next));

        List<BookingDto> list = bookingService.getByOwnerId(0, 20, user.getId(), "ALL");

        assertEquals(list.size(), 2);
    }
}