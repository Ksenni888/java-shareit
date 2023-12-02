package ru.practicum.shareit.item;

import org.apache.coyote.Request;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.exeption.ObjectNotFoundException;
import ru.practicum.shareit.exeption.ValidException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoForOwners;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

    @Test
    public void createTest() {
//        User user = User.builder()
//                .id(1L)
//                .name("Николай")
//                .email("nik@mail.ru")
//                .build();
//
//
//        ItemDto itemDto = ItemDto.builder()
//                .id(0L)
//                .name("item")
//                .description("description item")
//                .available(true)
//                .requestId(0)
//                .build();
//
//        Item item2= Item.builder()
//                .id(1L)
//                .name("item")
//                .description("description item")
//                .available(true)
//                .owner(user)
//                .request(null)
//                .build();
//
//        ItemDto itemDto2= ItemDto.builder()
//                .id(1L)
//                .name("item")
//                .description("description item")
//                .available(true)
//                .requestId(0)
//                .build();
//
//        Mockito.when(userRepository.existsById(1L)).thenReturn(true);
//
//        Item item = new Item();
//        item.setId(itemDto.getId());
//        item.setName(itemDto.getName());
//        item.setDescription(itemDto.getDescription());
//        item.setAvailable(itemDto.getAvailable());
//        item.setOwner(user); //(User) Mockito.when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user)));
//        item.setRequest(null);//itemDto.getRequestId() != 0 ? (ItemRequest) Mockito.when(itemRequestRepository.findById(itemDto.getRequestId())).thenReturn(null) : null);
//
//        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user));
//        Mockito.when(itemRequestRepository.findById(itemDto.getRequestId())).thenReturn(null);
//        Mockito.when(itemRepository.save(item)).thenReturn(item2);
//        Mockito.when(itemMapper.toItemDto(item2)).thenReturn(itemDto2);
//
//        ItemDto result = itemService.create(1L, itemDto);
//
//
//        Assertions.assertEquals(result, itemDto2);

    }

    @Test
    public void updateTest() {
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

        Item item1 = Item.builder()
                .id(1L)
                .name("item1")
                .description("description item")
                .available(true)
                .owner(user)
                .request(null)
                .build();

        Mockito.when(itemRepository.existsById(1L)).thenReturn(true);
        Mockito.when(itemRepository.getReferenceById(1L)).thenReturn(item);

        Mockito.when(itemRepository.save(item)).thenReturn(item1);

        Item result = itemService.update(1L, item, 1L);
        Assertions.assertEquals(item1, result);
    }

    public void findById() {
//        Mockito.when(itemRepository.existsById(1L)).thenReturn(true);
//
//
//        itemMapper.toItemDtoForOwners(itemRepository.getReferenceById(itemId), userId, lastBooking, nextBooking, comments)
//
//        1L, 1L
    }

}



