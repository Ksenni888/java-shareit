package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.item.model.Comments;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@SpringBootTest
@AutoConfigureTestDatabase
public class BookingIntegrationTest {
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookingServiceImpl bookingService;
    @Autowired
    private CommentRepository commentRepository;

    @Test
    public void getUserBookingsTest() {
        userRepository.deleteAll();
        User user = new User(0L, "userName", "user3@user.ru");
        User baseUser = userRepository.save(user);

        User user2 = User.builder()
                .id(0L)
                .name("Нико")
                .email("nik90@mail.ru")
                .build();
        User baseUser2 = userRepository.save(user2);

        Item item = Item.builder()
                .id(0L)
                .name("item")
                .description("description item")
                .available(true)
                .owner(baseUser)
                .request(null)
                .build();
        Item item1 = itemRepository.save(item);

        Booking baseBooking = new Booking();
        baseBooking.setId(0L);
        baseBooking.setStart(LocalDateTime.of(2023, Month.APRIL, 8, 12, 30));
        baseBooking.setEnd(LocalDateTime.of(2023, Month.APRIL, 10, 12, 30));
        baseBooking.setBooker(baseUser2);
        baseBooking.setItem(item1);
        baseBooking.setStatus(BookingStatus.APPROVED);

        Comments comments = new Comments();
        comments.setText("srzgezb");
        comments.setCreated(LocalDateTime.of(2023, Month.APRIL, 11, 12, 30));
        comments.setItem(item1);
        comments.setAuthor(baseUser2);

        bookingRepository.save(baseBooking);
        commentRepository.save(comments);

        List<Item> itemByOwnerId = itemRepository.findByOwnerId(baseUser.getId());

        List<Long> allItemsByUser = itemByOwnerId.stream()
                .map(Item::getId)
                .collect(Collectors.toList());
        List<Booking> baseBooking1 = bookingRepository.findByItemIdIn(allItemsByUser, PageRequest.of(0, 1, Sort.by("start").descending()));

        List<Booking> allUserBookings = bookingService.checkState(baseBooking1, "ALL");
        List<Booking> result = bookingService.getUserBookings(baseUser.getId(), "ALL", PageRequest.of(0, 1, Sort.by("start").descending()));

        Assertions.assertEquals(allUserBookings.get(0).getBooker().getId(), result.get(0).getBooker().getId());
    }
}