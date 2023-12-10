package ru.practicum.shareit.item;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoForOwners;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comments;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.Month;
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

    ItemDto itemDtoMapper = ItemDto.builder()
            .id(1L)
            .name("item")
            .description("description item")
            .available(true)
            .requestId(0)
            .build();

    @Test
    public void createTest() {
        User user2 = User.builder()
                .id(2L)
                .name("Нико")
                .email("nik7@mail.ru")
                .build();

        ItemRequest request = ItemRequest.builder()
                .id(1L)
                .description("item")
                .created(LocalDateTime.of(2023, Month.APRIL, 8, 12, 30))
                .user(user2)
                .build();

        Item item2 = Item.builder()
                .id(1L)
                .name("item")
                .description("description item")
                .available(true)
                .owner(user)
                .request(request)
                .build();

        ItemDto itemDto2 = ItemDto.builder()
                .id(1L)
                .name("item")
                .description("description item")
                .available(true)
                .requestId(1L)
                .build();

        ItemDto itemDto = ItemDto.builder()
                .id(0L)
                .name("item")
                .description("description item")
                .available(true)
                .requestId(1L)
                .build();

        when(userRepository.existsById(1L)).thenReturn(true);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(itemRequestRepository.findById(1L)).thenReturn(Optional.ofNullable(request));
        Item item = itemMapper.toItem(itemDto, user, request);

        when(itemRepository.save(item)).thenReturn(item2);
        when(itemMapper.toItemDto(item2)).thenReturn(itemDto2);

        ItemDto result = itemService.create(user.getId(), itemDto);

        Assertions.assertEquals(result, itemDto2);
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

        List<Item> result = itemService.findItems(text);
        Assertions.assertEquals(items, result);
    }

    @Test
    public void addCommentTest() {
        User user2 = User.builder()
                .id(2L)
                .name("Иван")
                .email("nik1@mail.ru")
                .build();

        Booking booking = Booking.builder()
                .id(1L)
                .start(LocalDateTime.of(2023, Month.APRIL, 8, 12, 30))
                .end(LocalDateTime.of(2023, Month.APRIL, 10, 12, 30))
                .item(item)
                .booker(user2)
                .status(BookingStatus.APPROVED)
                .build();

        CommentDto commentDto = CommentDto.builder()
                .text("comment1")
                .build();

        Comments comments1 = Comments.builder()
                .id(1L)
                .author(user2)
                .text("comment1")
                .item(item)
                .created(LocalDateTime.now())
                .build();

        Mockito.when(bookingRepository.findByBookerIdAndItemId(2L, 1L)).thenReturn(List.of(booking));

        Mockito.when(userRepository.getReferenceById(2L)).thenReturn(user);
        Mockito.when(itemRepository.getReferenceById(1L)).thenReturn(item);

        Comments comments = new Comments();
        when(itemMapper.toComment(commentDto, user, item)).thenReturn(comments);

        Mockito.when(commentRepository.save(comments)).thenReturn(comments1);

        Comments result = itemService.addComment(2L, 1L, commentDto);
        Assertions.assertEquals(result, comments1);
    }

    @Test
    public void itemMapperToItemDtoTest() {
        ItemMapper itemMapper1 = new ItemMapper();
        ItemDto result = itemMapper1.toItemDto(item);
        Assertions.assertEquals(result.getId(), itemDtoMapper.getId());
        Assertions.assertEquals(result.getName(), itemDtoMapper.getName());
        Assertions.assertEquals(result.getDescription(), itemDtoMapper.getDescription());
        Assertions.assertEquals(result.getAvailable(), itemDtoMapper.getAvailable());
        Assertions.assertEquals(result.getRequestId(), itemDtoMapper.getRequestId());
    }

    @Test
    public void toItem() {
        ItemMapper itemMapper2 = new ItemMapper();
        Item result = itemMapper2.toItem(itemDtoMapper, user, null);
        Assertions.assertEquals(result.getId(), item.getId());
        Assertions.assertEquals(result.getName(), item.getName());
        Assertions.assertEquals(result.getDescription(), item.getDescription());
        Assertions.assertEquals(result.getAvailable(), item.getAvailable());
        Assertions.assertEquals(result.getRequest(), item.getRequest());
    }
}