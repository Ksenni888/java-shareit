package ru.practicum.shareit.request;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceTest {
    @Mock
    private ItemRequestRepository itemRequestRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private ItemRequestMapper itemRequestMapper;
    @InjectMocks
    private ItemRequestServiceImpl itemRequestService;

    @Test
    public void findRequestByIdTest() {
        ItemRequest itemRequest = ItemRequest.builder().build();
        Item item = Item.builder().build();
        ItemRequestDto itemRequestDto = ItemRequestDto.builder().build();

        Mockito.when(userRepository.existsById(any())).thenReturn(true);
        Mockito.when(itemRequestRepository.existsById(any())).thenReturn(true);
        Mockito.when(itemRequestRepository.findById(1L)).thenReturn(Optional.ofNullable(itemRequest));

        assert itemRequest != null;
        Mockito.when(itemRepository.findByRequestId(itemRequest.getId())).thenReturn(List.of(item));
        List<Item> items = List.of(item);

        Mockito.when(itemRequestMapper.toDtoRequest(itemRequest, items)).thenReturn(itemRequestDto);

        ItemRequestDto result = itemRequestService.findRequestById(1L, 1L);

        Assertions.assertEquals(itemRequestDto, result);
    }

    @Test
    public void addRequestTest() {
        User user = new User();
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        ItemRequest saveItemRequest = new ItemRequest();
        ItemRequest itemRequest = new ItemRequest();
        Mockito.when(userRepository.existsById(1L)).thenReturn(true);
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        Mockito.when(itemRequestMapper.toRequest(itemRequestDto, user)).thenReturn(saveItemRequest);
        Mockito.when(itemRequestRepository.save(saveItemRequest)).thenReturn(itemRequest);

        ItemRequest result = itemRequestService.addRequest(1L, itemRequestDto);

        Assertions.assertEquals(itemRequest, result);
    }

    @Test
    public void getRequestTest() {
        Item item = new Item();
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(1L);
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        ItemRequestDto itemRequestDto1 = new ItemRequestDto();

        Mockito.when(userRepository.existsById(1L)).thenReturn(true);
        Mockito.when(itemRequestRepository.findByUserId(1L)).thenReturn(List.of(itemRequest));
        List<ItemRequest> saveItemRequests = List.of(itemRequest);

        Mockito.when(itemRepository.findByRequestId(1L)).thenReturn(List.of(item));
        List<Item> items = List.of(item);

        Mockito.when(itemRequestMapper.toDtoRequest(itemRequest, items)).thenReturn(itemRequestDto, itemRequestDto1);

        List<ItemRequestDto> itemRequestFinal = saveItemRequests.stream().map(x -> itemRequestDto)
                .sorted(Comparator.comparing((ItemRequestDto::getCreated)))
                .collect(Collectors.toList());

        List<ItemRequestDto> result = itemRequestService.getRequest(1L);

        Assertions.assertEquals(itemRequestFinal, result);
    }

    @Test
    public void getAllRequestsTest() {
        ItemRequest itemRequest = new ItemRequest();
        List<ItemRequest> allItemRequest = new ArrayList<>();
        Page<ItemRequest> allPage = new PageImpl<>(allItemRequest);

        Mockito.when(itemRequestRepository.findAll(PageRequest.of(0, 1, Sort.by("created").descending())))
                .thenReturn(allPage);

        List<ItemRequestDto> outputItemRequestDto =
                itemRequestRepository.findAll(PageRequest.of(0, 1, Sort.by("created").descending())).stream()
                        .filter(x -> x.getUser().getId() != 1L)
                        .map(x -> itemRequestMapper.toDtoRequest(x, itemRepository.findByRequestId(x.getId())))
                        .collect(Collectors.toList());

        List<ItemRequestDto> result = itemRequestService.getAllRequests(1L, PageRequest.of(0, 1, Sort.by("created").descending()));
        Assertions.assertEquals(outputItemRequestDto, result);
    }
}