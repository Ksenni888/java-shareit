package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.practicum.shareit.model.TestModels.*;

@DataJpaTest
class ItemRequestRepositoryTest {
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRequestRepository itemRequestRepository;

    private final Pageable pageable = PageRequest.of(0, 20, Sort.unsorted());

    private Item item1;
    private ItemRequest itemRequest1;

    @BeforeEach
    void setUp() {
        item.setOwner(userRepository.save(user));
        item1 = itemRepository.save(item);
        item.setOwner(user);
        itemRequest.setRequester(userRepository.save(booker));
        itemRequest1 = itemRequestRepository.save(itemRequest);
        itemRequest.setRequester(booker);
    }


    @Test
    @DisplayName("Получение всех запросов от создавшего запрос")
    void findAllByRequesterTest() {
        item1.setRequest(itemRequest1);
        itemRepository.save(item1);

        List<ItemRequest> list = itemRequestRepository.findAllByRequester(itemRequest1.getRequester());

        assertEquals(list.get(0), itemRequest1);
    }


    @Test
    @DisplayName("Получение всех запросов без id создавшего запрос")
    void findAllByRequesterIdIsNotTest() {
        item1.setRequest(itemRequest1);
        itemRepository.save(item1);

        List<ItemRequest> list = itemRequestRepository.findAllByRequesterIdIsNot(itemRequest1.getRequester().getId(),
                pageable);

        assertEquals(list.size(), 0);
    }
}