package ru.practicum.shareit.request;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class RequestRepositoryTest {
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRequestRepository itemRequestRepository;

    private final Pageable pageable = PageRequest.of(0, 20, Sort.unsorted());

    private Item item1;
    private ItemRequest itemRequest1;
    public static final User user = new User(1, "userName", "mail@email.ru");
    public static final Item item = new Item(1, "itemName", "itemDescription", true, user, null);

    public static final ItemRequest itemRequest = new ItemRequest(1, "text", user, LocalDateTime.of(2023, Month.APRIL, 8, 12, 30));
    public static final User booker = new User(2, "userName2", "email2@mail.ru");

    @BeforeEach
    void setUp() {
        item.setOwner(userRepository.save(user));
        item1 = itemRepository.save(item);
        item.setOwner(user);
        itemRequest.setUser(userRepository.save(booker));
        itemRequest1 = itemRequestRepository.save(itemRequest);
        itemRequest.setUser(booker);
    }

    @Test
    void findByUserIdTest() {
        item1.setRequest(itemRequest1);
        itemRepository.save(item1);

        List<ItemRequest> list = itemRequestRepository.findByUserId(itemRequest1.getUser().getId());

        assertEquals(list.get(0), itemRequest1);
    }
}