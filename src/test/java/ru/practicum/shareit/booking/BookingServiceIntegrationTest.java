//package ru.practicum.shareit.booking;
//
//import lombok.RequiredArgsConstructor;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Sort;
//import org.springframework.transaction.annotation.Transactional;
//import ru.practicum.shareit.booking.model.Booking;
//import ru.practicum.shareit.booking.model.BookingStatus;
//import ru.practicum.shareit.booking.repository.BookingRepository;
//import ru.practicum.shareit.booking.service.BookingServiceImpl;
//import ru.practicum.shareit.item.model.Comments;
//import ru.practicum.shareit.item.model.Item;
//import ru.practicum.shareit.item.repository.CommentRepository;
//import ru.practicum.shareit.item.repository.ItemRepository;
//import ru.practicum.shareit.user.model.User;
//import ru.practicum.shareit.user.repository.UserRepository;
//
//import java.time.LocalDateTime;
//import java.time.Month;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Transactional
//@SpringBootTest
//@AutoConfigureTestDatabase
//@RequiredArgsConstructor(onConstructor_ = @Autowired)
//public class BookingServiceIntegrationTest {
//    @Autowired
//    private final BookingRepository bookingRepository;
//    @Autowired
//    private final ItemRepository itemRepository;
//    @Autowired
//    private final UserRepository userRepository;
//    @Autowired
//    private final BookingServiceImpl bookingService;
//    @Autowired
//    private final CommentRepository commentRepository;
//
//    @Test
//    public void getUserBookingsTest() {
//        User user = new User(0L, "userName", "user@user.ru");
//        User saveUser = userRepository.save(user);
//
//        User user2 = User.builder()
//                .id(0L)
//                .name("Нико")
//                .email("nik7@mail.ru")
//                .build();
//        User saveUser2 = userRepository.save(user2);
//        saveUser2.setId(2L);
//
//        Item item = Item.builder()
//                .id(0L)
//                .name("item")
//                .description("description item")
//                .available(true)
//                .owner(saveUser)
//                .request(null)
//                .build();
//        Item item1 = itemRepository.save(item);
//
//        Booking saveBooking = new Booking();
//        saveBooking.setId(0L);
//        saveBooking.setStart(LocalDateTime.of(2023, Month.APRIL, 8, 12, 30));
//        saveBooking.setEnd(LocalDateTime.of(2023, Month.APRIL, 10, 12, 30));
//        saveBooking.setBooker(saveUser2);
//        saveBooking.setItem(item1);
//        saveBooking.setStatus(BookingStatus.APPROVED);
//
//        Comments comments = new Comments();
//        comments.setText("srzgezb");
//        comments.setCreated(LocalDateTime.of(2023, Month.APRIL, 11, 12, 30));
//        comments.setItem(item1);
//        comments.setAuthor(saveUser2);
//
//        bookingRepository.save(saveBooking);
//        commentRepository.save(comments);
//
//        List<Item> itemByOwnerId = itemRepository.findByOwnerId(1L);
//
//        List<Long> allItemsByUser = itemByOwnerId.stream()
//                .map(Item::getId)
//                .collect(Collectors.toList());
//        List<Booking> saveBooking1 = bookingRepository.findByItemIdIn(allItemsByUser, PageRequest.of(0, 1, Sort.by("start").descending()));
//
//        List<Booking> allUserBookings = bookingService.checkState(saveBooking1, "ALL");
//        List<Booking> result = bookingService.getUserBookings(1L, "ALL", PageRequest.of(0, 1, Sort.by("start").descending()));
//
//        Assertions.assertEquals(allUserBookings.get(0).getBooker(), result.get(0).getBooker());
//    }
//}