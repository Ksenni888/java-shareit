package ru.practicum.shareit.item;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemBookingDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static ru.practicum.shareit.model.JsonModels.itemDescription;
import static ru.practicum.shareit.model.JsonModels.itemName;
import static ru.practicum.shareit.model.TestModels.*;

@Transactional
@SpringBootTest
@AutoConfigureTestDatabase
public class ItemIntegrationTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private ItemService itemService;

    @Test
    @DisplayName("Получение всех вещей")
    void getItemAllTest() {
        User userNew = userRepository.save(user);
        Item item = itemRepository.save(new Item(1, itemName, itemDescription, true, userNew, null));
        List<ItemBookingDto> list = itemService.getAllByOwner(userNew.getId(), 0, 20);
        assertEquals(list.size(), 1);
        assertEquals(list.get(0).getId(), item.getId());
        assertEquals(list.get(0).getName(), item.getName());
        assertEquals(list.get(0).getAvailable(), item.getAvailable());
        assertEquals(list.get(0).getDescription(), item.getDescription());
        assertEquals(list.get(0).getComments().size(), 0);
    }

    @Test
    @DisplayName("Создание комментария")
    public void addCommentTest() {
        User userNew = userRepository.save(user);
        User requester = userRepository.save(booker);

        Item item = itemRepository.save(new Item(1, itemName, itemDescription, true, userNew, null));
        bookingRepository.save(new Booking(1, LocalDateTime.now().minusDays(2), LocalDateTime.now().minusDays(1),
                item, requester, Status.APPROVED));

        CommentDto commentDto1 = itemService.addComment(item.getId(), commentDto, requester.getId());

        assertNotNull(commentDto1);
        assertEquals(commentDto1.getAuthorName(), requester.getName());
        assertEquals(commentDto1.getItemId(), item.getId());
    }
}