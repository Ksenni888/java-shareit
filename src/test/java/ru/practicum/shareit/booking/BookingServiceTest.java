package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BookingMapper bookingMapper;
    @InjectMocks
    private BookingServiceImpl bookingService;

    @Test
    public void createBookingTest() {
        User user = new User();
        user.setId(1L);
        User user2 = new User();
        user2.setId(2L);

        Item item = new Item();
        item.setId(1L);
        item.setAvailable(true);
        item.setOwner(user2);

        BookingDto bookingDto = new BookingDto();
        bookingDto.setStart(LocalDateTime.of(2023, Month.APRIL, 8, 12, 30));
        bookingDto.setEnd(LocalDateTime.of(2023, Month.APRIL, 12, 12, 30));
        bookingDto.setItemId(1L);
        bookingDto.setStatus(null);

        Booking saveBooking = new Booking();
        saveBooking.setId(0L);
        saveBooking.setItem(item);
        saveBooking.setStart(LocalDateTime.of(2023, Month.APRIL, 8, 12, 30));
        saveBooking.setEnd(LocalDateTime.of(2023, Month.APRIL, 12, 12, 30));
        saveBooking.setBooker(user);
        saveBooking.setStatus(BookingStatus.WAITING);

        Booking saveBooking1 = new Booking();
        saveBooking.setId(1L);
        saveBooking.setItem(item);
        saveBooking.setStart(LocalDateTime.of(2023, Month.APRIL, 8, 12, 30));
        saveBooking.setEnd(LocalDateTime.of(2023, Month.APRIL, 12, 12, 30));
        saveBooking.setBooker(user);
        saveBooking.setStatus(BookingStatus.WAITING);

        Mockito.when(userRepository.existsById(1L)).thenReturn(true);
        Mockito.when(itemRepository.existsById(1L)).thenReturn(true);

        Mockito.when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        Mockito.when(bookingMapper.toBooking(bookingDto, user, item)).thenReturn(saveBooking);

        Mockito.when(bookingRepository.save(saveBooking)).thenReturn(saveBooking1);

        Booking result = bookingService.createBooking(1L, bookingDto);
        Assertions.assertEquals(saveBooking1, result);
    }

    @Test
    public void checkRequestTest() {
        String approved = "true";
        User user = new User();
        user.setId(1L);

        User user2 = new User();
        user2.setId(2L);

        Item item = new Item();
        item.setId(1L);
        item.setOwner(user);
        item.setAvailable(true);

        Booking saveBooking = new Booking();
        saveBooking.setId(1L);
        saveBooking.setBooker(user2);
        saveBooking.setItem(item);
        saveBooking.setStatus(BookingStatus.WAITING);

        Mockito.when(bookingRepository.existsById(1L)).thenReturn(true);
        Mockito.when(bookingRepository.findById(1L)).thenReturn(Optional.of(saveBooking));
        Mockito.when(userRepository.existsById(1L)).thenReturn(true);

        Booking result = bookingService.checkRequest(1L, 1L, approved);

        Assertions.assertEquals(saveBooking, result);
    }

    @Test
    public void getBookingTest(long userId, long bookingId) {}

    }
