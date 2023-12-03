package ru.practicum.shareit.item;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoForOwners;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comments;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTests {
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private ItemRequestRepository itemRequestRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private ItemMapper itemMapper;

    @InjectMocks
    private ItemServiceImpl itemService;

    User user = User.builder()
            .id(1L)
            .name("Николай")
            .email("nik@mail.ru")
            .build();

    Item item = Item.builder()
            .id(1L)
            .name("item")
            .description("description item")
            .available(true)
            .owner(user)
            .request(null)
            .build();

    @Test
    public void createTest() {
//        User user = User.builder()
//                .id(1L)
//                .name("Николай")
//                .email("nik@mail.ru")
//                .build();
//
//        Item item2 = Item.builder()
//                .id(1L)
//                .name("item")
//                .description("description item")
//                .available(true)
//                .owner(user)
//                .request(null)
//                .build();
//
//        ItemDto itemDto = ItemDto.builder()
//                .id(0L)
//                .name("item")
//                .description("description item")
//                .available(true)
//                .requestId(0)
//                .build();
//
//        ItemDto itemDto2 = ItemDto.builder()
//                .id(1L)
//                .name("item")
//                .description("description item")
//                .available(true)
//                .requestId(0)
//                .build();
//        when(userRepository.existsById(1L)).thenReturn(true);
//
//        Item saveItem = new Item();
//        saveItem.setId(0L);
//        saveItem.setName("item");
//        saveItem.setDescription("description item");
//        saveItem.setAvailable(true);
//        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
//        saveItem.setOwner(user);
//        when(itemRequestRepository.findById(itemDto.getRequestId())).thenReturn(null);
//        saveItem.setRequest(null);
//
//        when(itemRepository.save(saveItem)).thenReturn(item2);
//        when(itemMapper.toItemDto(item2)).thenReturn(itemDto2);
//
//        ItemDto result = itemService.create(1L, itemDto);
//
//        Assertions.assertEquals(result, itemDto2);

    }

    @Test
    public void updateTest() {

        Item item1 = Item.builder()
                .id(1L)
                .name("item1")
                .description("description item")
                .available(true)
                .owner(user)
                .request(null)
                .build();

        when(itemRepository.existsById(1L)).thenReturn(true);
        when(itemRepository.getReferenceById(1L)).thenReturn(item);

        when(itemRepository.save(item)).thenReturn(item1);

        Item result = itemService.update(1L, item, 1L);
        Assertions.assertEquals(item1, result);
    }

    @Test
    public void findByIdTest() {
        List<Booking> bookings = new ArrayList<>();
        when(itemRepository.existsById(1L)).thenReturn(true);
        when(bookingRepository.findByItemIdAndStatus(1L, BookingStatus.APPROVED)).thenReturn(bookings);

        List<Comments> comments = new ArrayList<>();
        when(commentRepository.findByItemId(1L)).thenReturn(comments);

        List<CommentDto> commentsDto = new ArrayList<>();

        ItemDtoForOwners itemDtoForOwners = ItemDtoForOwners.builder()
                .id(1L)
                .name("item")
                .description("description item")
                .available(true)
                .request(0)
                .lastBooking(null)
                .nextBooking(null)
                .comments(commentsDto)
                .build();

        when(itemRepository.getReferenceById(1L)).thenReturn(item);
        when(itemMapper.toItemDtoForOwners(item, 1L, null, null, commentsDto)).thenReturn(itemDtoForOwners);

        ItemDtoForOwners result = itemService.findById(1L, 1L);

        Assertions.assertEquals(itemDtoForOwners, result);
    }

    @Test
    public void getItemsByUserIdTest() {
        when(userRepository.existsById(1L)).thenReturn(true);
        when(itemRepository.existsById(1L)).thenReturn(true);
        List<Item> items = List.of(item);
        Mockito.when(itemRepository.findByOwnerId(1L)).thenReturn(items);
        List<ItemDtoForOwners> outputItems = items.stream()
                .map(x -> itemService.findById(x.getId(), user.getId()))
                .collect(Collectors.toList());

        List<ItemDtoForOwners> result = itemService.getItemsByUserId(1L);
        Assertions.assertEquals(outputItems, result);
    }

    @Test
    public void findItemsTest() {
        String text = "tekst";
        List<Item> items = List.of(item);
        Mockito.when(itemRepository.search(text)).thenReturn(items);
        items.stream()
                .filter(Item::getAvailable)
                .collect(Collectors.toList());

        List<Item> result = itemService.findItems(text);
        Assertions.assertEquals(items, result);
    }

    @Test
    public void addCommentTest() {

    }

}



