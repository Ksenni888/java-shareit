package ru.practicum.shareit.item;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.dto.ItemBookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static ru.practicum.shareit.model.JsonModels.*;
import static ru.practicum.shareit.model.TestModels.*;

@Transactional
@ExtendWith(MockitoExtension.class)
@ComponentScan(basePackages = {"ru.yandex.practicum.shareit"})
class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserService userService;
    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private ItemServiceImpl itemService;


    @Test
    @DisplayName("Создание вещи")
    void addItem() {
        when(userService.getById(anyInt())).thenReturn(UserMapper.toUserDto(booker));
        when(itemRepository.save(any())).thenReturn(item);

        ItemDto itemDto1 = itemService.create(itemDto, 1);

        assertEquals(itemDto1.getName(), itemName);
        assertEquals(itemDto1.getDescription(), itemDescription);
    }

    @Test
    @DisplayName("Обновление вещи")
    void updateItem() {
        when(itemRepository.save(any())).thenReturn(itemUpd);
        when(itemRepository.getReferenceById(anyInt())).thenReturn(item);

        ItemDto newItem = itemService.update(itemDtoUpd, 1, item.getId());

        assertEquals(newItem.getName(), itemName2);
        assertEquals(newItem.getDescription(), itemDescription2);
    }

    @Test
    @DisplayName("Удаление вещи")
    void deleteItem() {
        assertDoesNotThrow(() -> itemService.delete(1));
    }

    @Test
    @DisplayName("Получение вещи")
    void getItem() {
        when(itemRepository.findById(anyInt())).thenReturn(item);
        when(commentRepository.findAllByItemId(anyInt())).thenReturn(List.of(comment));
        when(bookingRepository.findFirstByItemIdAndStartBeforeAndStatusOrderByEndDesc(anyInt(), any(), any()))
                .thenReturn(Optional.of(last));
        when(bookingRepository.findFirstByItemIdAndStartAfterAndStatusOrderByStartAsc(anyInt(), any(), any()))
                .thenReturn(Optional.of(next));


        ItemBookingDto newItem = itemService.getItem(item.getId(), user.getId());

        assertEquals(newItem.getId(), item.getId());
        assertEquals(newItem.getNextBooking(), nextItemWithBooking);
        assertEquals(newItem.getLastBooking(), lastItemWithBooking);
        assertEquals(newItem.getName(), itemName);
        assertEquals(newItem.getAvailable(), available);
        assertEquals(newItem.getComments(), List.of(commentDto));

    }

    @Test
    @DisplayName("Поиск вещей")
    void searchItems() {
        when(itemRepository.findItemByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(any(), any(), any())).thenReturn(List.of(item));

        List<ItemDto> list = itemService.search("дрель", 0, 20);

        assertEquals(list.size(), 1);
        assertEquals(list.get(0).getName(), item.getName());
    }

    @Test
    @DisplayName("Получение списка всех вещей")
    void getItems() {
        when(userService.getById(anyInt())).thenReturn(UserMapper.toUserDto(booker));
        when(itemRepository.findByOwnerId(anyInt(), any())).thenReturn(List.of(item));
        ItemMapper.toItemDto(new Item(10, itemName, itemDescription, false, user, itemRequest));

        List<ItemBookingDto> list = itemService.getAllByOwner(1, 0, 20);

        assertEquals(list.size(), 1);
        assertEquals(list.get(0).getName(), itemName);
        assertEquals(list.get(0).getId(), 1);
    }
}