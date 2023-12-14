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
import ru.practicum.shareit.exeption.ObjectNotFoundException;
import ru.practicum.shareit.exeption.ValidException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;

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

    User user = new User(1L, "userName", "email@mail.ru");
    User user2 = new User(2L, "userName2", "email2@mail.ru");
    Item item = new Item(1L, "itemName", "itemDescription", true, user, null);
    Item item1 = new Item(2L, "itemName2", "itemDescription2", true, user, null);
    LocalDateTime start = LocalDateTime.of(2024, Month.APRIL, 8, 12, 30);
    LocalDateTime end = LocalDateTime.of(2024, Month.APRIL, 12, 12, 30);
    Item itemAvailableFalse = new Item(3L, "itemName", "itemDescription", false, user, null);
    BookingDto bookingDtoItemAvailableFalse = new BookingDto(1L, 3L, start, end, BookingStatus.APPROVED);
    Booking booking = new Booking(1L, start, end, item, user, BookingStatus.APPROVED);
    Booking saveBooking = new Booking(1L, start, end, item, user2, BookingStatus.WAITING);
    BookingDto bookingDto = new BookingDto(0L, 1L, LocalDateTime.of(2024, Month.APRIL, 8, 12, 30),
            LocalDateTime.of(2024, Month.APRIL, 12, 12, 30), BookingStatus.WAITING);
    BookingDto bookingDtoTime = new BookingDto(0L, 1L, LocalDateTime.of(2024, Month.APRIL, 8, 12, 30),
            LocalDateTime.of(2023, Month.APRIL, 12, 12, 30), BookingStatus.APPROVED);
    BookingDto bookingDtoEqualTime = new BookingDto(0L, 1L, LocalDateTime.of(2023, Month.APRIL, 12, 12, 30),
            LocalDateTime.of(2023, Month.APRIL, 12, 12, 30), BookingStatus.APPROVED);

    @Test
    public void createBookingTest() {

        Booking baseBooking = new Booking();
        baseBooking.setId(0L);
        baseBooking.setItem(item);
        baseBooking.setStart(LocalDateTime.of(2024, Month.APRIL, 8, 12, 30));
        baseBooking.setEnd(LocalDateTime.of(2024, Month.APRIL, 12, 12, 30));
        baseBooking.setBooker(user2);
        baseBooking.setStatus(BookingStatus.WAITING);

        Booking baseBooking1 = new Booking();
        baseBooking1.setId(1L);
        baseBooking1.setItem(item);
        baseBooking1.setStart(LocalDateTime.of(2024, Month.APRIL, 8, 12, 30));
        baseBooking1.setEnd(LocalDateTime.of(2024, Month.APRIL, 12, 12, 30));
        baseBooking1.setBooker(user2);
        baseBooking1.setStatus(BookingStatus.WAITING);

        Mockito.when(userRepository.existsById(2L)).thenReturn(true);
        Mockito.when(itemRepository.existsById(1L)).thenReturn(true);

        Mockito.when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        Mockito.when(userRepository.findById(2L)).thenReturn(Optional.of(user2));

        Mockito.when(bookingMapper.toBooking(bookingDto, user2, item)).thenReturn(baseBooking);
        Mockito.when(bookingRepository.save(baseBooking)).thenReturn(baseBooking1);

        Booking result = bookingService.createBooking(2L, bookingDto);
        Assertions.assertEquals(baseBooking1.getId(), result.getId());
    }

    @Test
    public void createBookingEndBeforeStartTimeTest() {
        final ValidException exception = Assertions.assertThrows(
                ValidException.class,
                () -> bookingService.createBooking(user.getId(), bookingDtoTime));

        Assertions.assertEquals("Data start can't be later then end", exception.getMessage());
    }

    @Test
    public void createBookingEqualTimeTest() {
        final ValidException exception = Assertions.assertThrows(
                ValidException.class,
                () -> bookingService.createBooking(user.getId(), bookingDtoEqualTime));

        Assertions.assertEquals("Dates start and end can be different", exception.getMessage());
    }

    @Test
    public void createBookingNotFoundUserTest() {
        Mockito.when(userRepository.existsById(user.getId())).thenReturn(false);

        final ObjectNotFoundException exception = Assertions.assertThrows(
                ObjectNotFoundException.class,
                () -> bookingService.createBooking(user.getId(), bookingDto));

        Assertions.assertEquals("This user is not exist", exception.getMessage());
    }

    @Test
    public void createBookingNotExistItemTest() {
        Mockito.when(userRepository.existsById(user.getId())).thenReturn(true);
        Mockito.when(itemRepository.existsById(item.getId())).thenReturn(false);

        final ObjectNotFoundException exception = Assertions.assertThrows(
                ObjectNotFoundException.class,
                () -> bookingService.createBooking(user.getId(), bookingDto));

        Assertions.assertEquals("This item is not exist", exception.getMessage());
    }

    @Test
    public void createBookingEmptyItemTest() {
        Mockito.when(itemRepository.findById(item.getId()).isEmpty()).thenThrow(new ObjectNotFoundException("Item not found"));

        final ObjectNotFoundException exception = Assertions.assertThrows(
                ObjectNotFoundException.class,
                () -> itemRepository.findById(item.getId()).isEmpty());

        Assertions.assertEquals("Item not found", exception.getMessage());
    }

    @Test
    public void createBookingNotAvailableItemTest() {
        Mockito.when(userRepository.existsById(user.getId())).thenReturn(true);
        Mockito.when(itemRepository.existsById(itemAvailableFalse.getId())).thenReturn(true);
        Mockito.when(itemRepository.findById(anyLong())).thenReturn(Optional.ofNullable(itemAvailableFalse));
        final ValidException exception = Assertions.assertThrows(
                ValidException.class,
                () -> bookingService.createBooking(user.getId(), bookingDtoItemAvailableFalse));

        Assertions.assertEquals("Item is not available", exception.getMessage());
    }

    @Test
    public void checkRequestTest() {
        String approved = "true";
        Mockito.when(bookingRepository.existsById(1L)).thenReturn(true);
        Mockito.when(bookingRepository.findById(1L)).thenReturn(Optional.of(saveBooking));
        Mockito.when(userRepository.existsById(1L)).thenReturn(true);

        Booking result = bookingService.checkRequest(1L, 1L, approved);

        Assertions.assertEquals(saveBooking, result);
    }

    @Test
    public void checkRequestApprovedIsBlankTest() {
        String approved = "";
        ValidException exception = Assertions.assertThrows(
                ValidException.class,
                () -> bookingService.checkRequest(user.getId(), booking.getId(), approved));
        Assertions.assertEquals("approved must be true/false", exception.getMessage());
    }

    @Test
    public void getBookingTest() {
        Mockito.when(userRepository.existsById(1L)).thenReturn(true);
        Mockito.when(bookingRepository.existsById(1L)).thenReturn(true);
        Mockito.when(bookingRepository.findById(1L)).thenReturn(Optional.of(saveBooking));

        Booking result = bookingService.getBooking(1L, 1L);
        Assertions.assertEquals(saveBooking, result);
    }

    @Test
    public void getBookingsByStatusTest() {
        List<Booking> bookingsByUserId = new ArrayList<>();
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

    @Test
    public void checkStateTest() {
        User user = new User(1L, "userName", "email@mail.ru");
        Item item = new Item(1L, "itemName", "itemDescription", true, user, null);
        Booking bookingCURRENT = new Booking(1L, LocalDateTime.now().minusDays(2), LocalDateTime.now().plusDays(2), item, user, BookingStatus.APPROVED);
        Booking bookingPAST = new Booking(2L, LocalDateTime.now().minusDays(7), LocalDateTime.now().minusDays(4), item, user, BookingStatus.APPROVED);
        Booking bookingFUTURE = new Booking(3L, LocalDateTime.now().plusDays(3), LocalDateTime.now().plusDays(5), item, user, BookingStatus.APPROVED);
        Booking bookingWAITING = new Booking(4L, LocalDateTime.now().minusHours(2), LocalDateTime.now().plusDays(4), item, user, BookingStatus.WAITING); //current
        Booking bookingREJECTED = new Booking(5L, LocalDateTime.now().minusDays(3), LocalDateTime.now().minusDays(1), item, user, BookingStatus.REJECTED); //past
        List<Booking> allBooking = List.of(bookingCURRENT, bookingFUTURE, bookingREJECTED, bookingPAST, bookingWAITING);

        List<Booking> resultCURRENT = bookingService.checkState(allBooking, "CURRENT");
        Assertions.assertEquals(resultCURRENT.size(), 2);

        List<Booking> resultPAST = bookingService.checkState(allBooking, "PAST");
        Assertions.assertEquals(resultPAST.size(), 2);

        List<Booking> resultFUTURE = bookingService.checkState(allBooking, "FUTURE");
        Assertions.assertEquals(resultFUTURE.get(0).getId(), bookingFUTURE.getId());

        List<Booking> resultWAITING = bookingService.checkState(allBooking, "WAITING");
        Assertions.assertEquals(resultWAITING.get(0).getId(), bookingWAITING.getId());

        List<Booking> resultREJECTED = bookingService.checkState(allBooking, "REJECTED");
        Assertions.assertEquals(resultREJECTED.get(0).getId(), bookingREJECTED.getId());
    }
}