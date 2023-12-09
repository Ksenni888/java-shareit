package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
import java.util.ArrayList;
import java.util.List;
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

        Booking baseBooking = new Booking();
        baseBooking.setId(0L);
        baseBooking.setItem(item);
        baseBooking.setStart(LocalDateTime.of(2023, Month.APRIL, 8, 12, 30));
        baseBooking.setEnd(LocalDateTime.of(2023, Month.APRIL, 12, 12, 30));
        baseBooking.setBooker(user);
        baseBooking.setStatus(BookingStatus.WAITING);

        Booking baseBooking1 = new Booking();
        baseBooking1.setId(1L);
        baseBooking1.setItem(item);
        baseBooking1.setStart(LocalDateTime.of(2023, Month.APRIL, 8, 12, 30));
        baseBooking1.setEnd(LocalDateTime.of(2023, Month.APRIL, 12, 12, 30));
        baseBooking1.setBooker(user);
        baseBooking1.setStatus(BookingStatus.WAITING);

        Mockito.when(userRepository.existsById(1L)).thenReturn(true);
        Mockito.when(itemRepository.existsById(1L)).thenReturn(true);

        Mockito.when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        Mockito.when(bookingMapper.toBooking(bookingDto, user, item)).thenReturn(baseBooking);

        Mockito.when(bookingRepository.save(baseBooking)).thenReturn(baseBooking1);

        Booking result = bookingService.createBooking(1L, bookingDto);
        Assertions.assertEquals(baseBooking1, result);
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
    public void getBookingTest() {
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

        Mockito.when(userRepository.existsById(1L)).thenReturn(true);
        Mockito.when(bookingRepository.existsById(1L)).thenReturn(true);
        Mockito.when(bookingRepository.findById(1L)).thenReturn(Optional.of(saveBooking));

        Booking result = bookingService.getBooking(1L, 1L);
        Assertions.assertEquals(saveBooking, result);
    }

    @Test
    public void getBookingsByStatusTest() {
        List<Booking> bookingsByUserId = new ArrayList<>();

        User user = new User();
        user.setId(1L);

        Mockito.when(userRepository.existsById(1L)).thenReturn(true);
        Mockito.when(bookingRepository.findByBookerId(1L, PageRequest.of(0, 1,
                Sort.by("start").descending()))).thenReturn(bookingsByUserId);

        List<Booking> allBookings = bookingService.checkState(bookingsByUserId, "ALL");

        List<Booking> result = bookingService.getBookingsByStatus(1L, "ALL", PageRequest.of(0, 1,
                Sort.by("start").descending()));

        Assertions.assertEquals(allBookings, result);
    }

    @Test
    public void getUserBookingsTest() {
        Item item = new Item();
        item.setId(1L);
        Item item1 = new Item();
        item1.setId(2L);
        List<Item> itemByOwnerId = List.of(item, item1);

        Mockito.when(itemRepository.findByOwnerId(1L)).thenReturn(itemByOwnerId);
        List<Booking> saveBooking = new ArrayList<>();
        List<Long> allItemsByUser = List.of(1L, 2L);

        Mockito.when(bookingRepository.findByItemIdIn(allItemsByUser, PageRequest.of(0, 1,
                Sort.by("start").descending()))).thenReturn(saveBooking);

        List<Booking> allBooking = bookingService.checkState(saveBooking, "ALL");
        List<Booking> result = bookingService.getUserBookings(1L, "ALL", PageRequest.of(0, 1,
                Sort.by("start").descending()));

        Assertions.assertEquals(allBooking, result);
    }
}