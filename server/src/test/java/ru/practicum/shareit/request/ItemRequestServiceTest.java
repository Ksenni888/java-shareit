package ru.practicum.shareit.request;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static ru.practicum.shareit.model.TestModels.*;

@Transactional
@ExtendWith(MockitoExtension.class)
@ComponentScan(basePackages = {"ru.yandex.practicum.shareit"})
class ItemRequestServiceTest {

    @Mock
    private ItemRequestRepository itemRequestRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserService userService;
    @InjectMocks
    private ItemRequestServiceImpl itemRequestService;

    @Test
    @DisplayName("Получение всех запросов")
    void getItemRequestsAllTest() {
        when(userService.getById(anyInt())).thenReturn(UserMapper.toUserDto(booker));

        List<ItemRequestDto> list = itemRequestService.getRequestsByRequester(anyInt());

        assertEquals(list, new ArrayList<>());
    }

    @Test
    @DisplayName("Получение всех запросов создавшим запрос")
    void getItemRequestsForRequester() {
        when(userService.getById(anyInt())).thenReturn(UserMapper.toUserDto(booker));
        when(itemRepository.findByRequest_Id(anyInt())).thenReturn(new ArrayList<>());
        when(itemRequestRepository.findAllByRequester(any())).thenReturn(List.of(itemRequest));

        List<ItemRequestDto> list = itemRequestService.getRequestsByRequester(anyInt());

        assertEquals(list.size(), 1);
    }

    @Test
    @DisplayName("Получение запроса")
    void getItemRequest() {
        when(userService.getById(anyInt())).thenReturn(UserMapper.toUserDto(booker));
        when(itemRequestRepository.findById(anyInt())).thenReturn(Optional.of(itemRequest));

        ItemRequestDto requestDto = itemRequestService.getRequestById(itemRequest.getId(), 1);

        assertEquals(requestDto.getDescription(), itemRequestDto.getDescription());
        assertEquals(requestDto.getCreated(), itemRequestDto.getCreated());
    }

    @Test
    @DisplayName("Создание запроса")
    void addItemRequestTest() {
        when(userService.getById(anyInt())).thenReturn(UserMapper.toUserDto(booker));
        when(itemRequestRepository.save(any())).thenReturn(itemRequest);

        ItemRequestDto itemRequestDto1 = itemRequestService.addRequest(itemRequestDto, user.getId());

        assertEquals(itemRequestDto1.getDescription(), itemRequestDto.getDescription());
    }
}