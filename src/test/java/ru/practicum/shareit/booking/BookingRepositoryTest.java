package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class BookingRepositoryTest {
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;

    private User user() {
        User user = new User();
        user.setId(1L);
        user.setName("name");
        user.setEmail("mail1@mail.ru");
        return user;
    }

    private User user2() {
        User user2 = new User();
        user2.setId(2L);
        user2.setName("name2");
        user2.setEmail("mail2@mail.ru");
        return user2;
    }

    private Item item() {
        Item item = new Item();
        item.setId(0L);
        item.setName("name");
        item.setDescription("desc");
        item.setAvailable(true);
        item.setOwner(user2());
        item.setRequest(null);
        return item;
    }

    private Booking booking() {
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setStart(LocalDateTime.of(2023, Month.APRIL, 8, 12, 30));
        booking.setEnd(LocalDateTime.of(2023, Month.APRIL, 10, 12, 30));
        booking.setItem(item());
        booking.setBooker(user());
        booking.setStatus(BookingStatus.WAITING);
        return booking;
    }

    @Test
    public void findByBookerIdTest() {
        userRepository.save(user());
        userRepository.save(user2());
        itemRepository.save(item());
        bookingRepository.save(booking());
        List<Booking> result = bookingRepository.findByBookerId(1L, PageRequest.of(0, 1,
                Sort.by("start").descending()));
        assertEquals(result.size(), 1);
        assertEquals(result.get(0).getBooker().getId(), user().getId());
    }
}