package ru.practicum.shareit.booking;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.practicum.shareit.model.JsonModels.itemDescription;
import static ru.practicum.shareit.model.JsonModels.itemName;
import static ru.practicum.shareit.model.TestModels.booker;
import static ru.practicum.shareit.model.TestModels.user;


@Transactional
@SpringBootTest
@AutoConfigureTestDatabase
public class BookingIntegrationTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private BookingService bookingService;

    @Test
    @DisplayName("Получение бронирования по id пользователя")
    void bookingGetByUserTest() {
        User user1 = userRepository.save(user);
        User requester = userRepository.save(booker);

        Item item = itemRepository.save(new Item(1, itemName, itemDescription, true, user1, null));
        Booking booking = bookingRepository.save(
                new Booking(1,
                        LocalDateTime.now().minusDays(2),
                        LocalDateTime.now().minusDays(1),
                        item,
                        requester,
                        Status.APPROVED)
        );

        List<BookingDto> list = bookingService.getByUserId(0, 20, requester.getId(), "ALL");
        assertEquals(list.get(0).getItem().getId(), item.getId());
        assertEquals(list.get(0).getItem().getName(), item.getName());
        assertEquals(list.get(0).getId(), booking.getId());
        assertEquals(list.get(0).getStatus(), booking.getStatus());
    }
}