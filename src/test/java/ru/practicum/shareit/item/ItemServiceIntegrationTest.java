//package ru.practicum.shareit.item;
//
//import lombok.RequiredArgsConstructor;
//import org.junit.Before;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//import ru.practicum.shareit.booking.model.Booking;
//import ru.practicum.shareit.booking.model.BookingStatus;
//import ru.practicum.shareit.booking.repository.BookingRepository;
//import ru.practicum.shareit.item.dto.ItemDtoForOwners;
//import ru.practicum.shareit.item.model.Comments;
//import ru.practicum.shareit.item.model.Item;
//import ru.practicum.shareit.item.repository.CommentRepository;
//import ru.practicum.shareit.item.repository.ItemRepository;
//import ru.practicum.shareit.item.service.ItemServiceImpl;
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
//public class ItemServiceIntegrationTest {
//    @Autowired
//    private ItemRepository itemRepository;
//    @Autowired
//    private UserRepository userRepository;
//    @Autowired
//    private BookingRepository bookingRepository;
//    @Autowired
//    private CommentRepository commentRepository;
//    @Autowired
//    private ItemServiceImpl itemService;
//
//    @Test
//    public void getItemsByUserId() {
//
//        User user = new User(0L, "userName", "user@user.ru");
//        User saveUser = userRepository.save(user);
//
//        User user2 = User.builder()
//                .id(0L)
//                .name("Нико")
//                .email("nik7@mail.ru")
//                .build();
//
//        User saveUser2 = userRepository.save(user2);
//
//        System.out.println("saveUser.getId()  " + saveUser.getId());
//        System.out.println("saveUser2.getId()  " + saveUser2.getId());
//
//        Item item = Item.builder()
//                .id(0L)
//                .name("item")
//                .description("description item")
//                .available(true)
//                .owner(saveUser)
//                .request(null)
//                .build();
//
//        Item item1 = itemRepository.save(item);
//
//        Booking saveBooking = new Booking();
//        saveBooking.setId(1L);
//        saveBooking.setStart(LocalDateTime.of(2023, Month.APRIL, 8, 12, 30));
//        saveBooking.setEnd(LocalDateTime.of(2023, Month.APRIL, 10, 12, 30));
//        saveBooking.setBooker(saveUser2);
//        saveBooking.setItem(item1);
//        saveBooking.setStatus(BookingStatus.APPROVED);
//        bookingRepository.save(saveBooking);
//
//        Booking lastBooking = new Booking();
//        lastBooking.setId(2L);
//        lastBooking.setStart(LocalDateTime.of(2024, Month.MARCH, 8, 12, 30));
//        lastBooking.setEnd(LocalDateTime.of(2024, Month.MARCH, 10, 12, 30));
//        lastBooking.setBooker(saveUser2);
//        lastBooking.setItem(item1);
//        lastBooking.setStatus(BookingStatus.WAITING);
//        bookingRepository.save(lastBooking);
//
//        Comments comments = new Comments();
//        comments.setText("srzgezb");
//        comments.setCreated(LocalDateTime.of(2023, Month.APRIL, 11, 12, 30));
//        comments.setItem(item1);
//        comments.setAuthor(saveUser2);
//        commentRepository.save(comments);
//
//        List<ItemDtoForOwners> allItems = itemRepository.findByOwnerId(1L).stream()
//                .map(x -> itemService.findById(x.getId(), 1L))
//                .collect(Collectors.toList());
//
//        List<ItemDtoForOwners> result = itemService.getItemsByUserId(1L);
//
//        Assertions.assertEquals(allItems.get(0).getId(), result.get(0).getId());
//        Assertions.assertEquals(allItems.get(0).getName(), result.get(0).getName());
//        Assertions.assertEquals(allItems.get(0).getDescription(), result.get(0).getDescription());
//        Assertions.assertEquals(allItems.get(0).getAvailable(), result.get(0).getAvailable());
//        Assertions.assertEquals(allItems.get(0).getRequest(), result.get(0).getRequest());
//        Assertions.assertEquals(allItems.get(0).getLastBooking().getId(), result.get(0).getLastBooking().getId());
//        Assertions.assertEquals(allItems.get(0).getNextBooking(), result.get(0).getNextBooking());
//        Assertions.assertEquals(allItems.get(0).getComments().get(0).getText(), result.get(0).getComments().get(0).getText());
//    }
//}