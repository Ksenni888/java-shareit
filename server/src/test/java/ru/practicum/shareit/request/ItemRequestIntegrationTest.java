package ru.practicum.shareit.request;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.practicum.shareit.model.JsonModels.itemDescription;
import static ru.practicum.shareit.model.JsonModels.itemName;
import static ru.practicum.shareit.model.TestModels.booker;
import static ru.practicum.shareit.model.TestModels.user;


@Transactional
@SpringBootTest
@AutoConfigureTestDatabase
public class ItemRequestIntegrationTest {

    @Autowired
    private ItemRequestRepository itemRequestRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ItemRequestService itemRequestService;

    @Test
    @DisplayName("Получение всех запросов")
    void getItemRequestsAllTest() {
        User userNew = userRepository.save(user);
        User requester = userRepository.save(booker);
        ItemRequest itemRequest = itemRequestRepository
                .save(new ItemRequest(1, "1", requester, LocalDateTime.now().plusDays(1), new ArrayList<>()));

        itemRepository.save(new Item(1, itemName, itemDescription, true, userNew, itemRequest));

        List<ItemRequestDto> list = itemRequestService.getAllRequests(userNew.getId(), 0, 20);

        assertEquals(list.size(), 1);
    }
}