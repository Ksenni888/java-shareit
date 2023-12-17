package ru.practicum.shareit.request.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class ItemRequestServiceImpl implements ItemRequestService {

    private ItemRequestRepository itemRequestRepository;
    private UserService userService;
    private ItemService itemService;
    private ItemRepository itemRepository;

    @Transactional
    @Override
    public ItemRequestDto addRequest(ItemRequestDto itemRequestDto, int requesterId) {
        log.info("Запрос на добавление запроса от пользователя с id {}", requesterId);
        User user = UserMapper.fromUserDto(userService.getById(requesterId));
        return ItemRequestMapper.toItemRequestDto(itemRequestRepository.save(ItemRequestMapper.fromItemRequestDto(
                user,
                itemRequestDto,
                null)));
    }

    @Override
    public ItemRequestDto getRequestById(int requestId, int requesterId) {
        log.info("Запрос на получение запроса с id {}", requestId);
        User user = UserMapper.fromUserDto(userService.getById(requesterId));
        ItemRequest itemRequest = itemRequestRepository.findById(requestId).orElseThrow(
                () -> new UserNotFoundException("Такого запроса не сущесвует"));
        itemRequest.setItems(new ArrayList<>(itemRepository.findByRequest_Id(requestId)));
        return ItemRequestMapper.toItemRequestDto(itemRequest);
    }

    @Override
    public List<ItemRequestDto> getRequestsByRequester(int requesterId) {
        log.info("Запрос на получение запросов от пользователя с id {}", requesterId);
        User user = UserMapper.fromUserDto(userService.getById(requesterId));
        return itemRequestRepository.findAllByRequester(user).stream()
                .peek(i -> i.setItems(new ArrayList<>(itemRepository.findByRequest_Id(i.getId()))))
                .map(ItemRequestMapper::toItemRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemRequestDto> getAllRequests(int requesterId, int from, int size) {
        log.info("Запрос на получение всех запросов от пользователя с id {}", requesterId);
        Pageable pageable = PageRequest.of(from, size, Sort.by("created").ascending());
        return itemRequestRepository.findAllByRequesterIdIsNot(requesterId, pageable).stream()
                .peek(i -> i.setItems(new ArrayList<>(itemRepository.findByRequest_Id(i.getId()))))
                .map(ItemRequestMapper::toItemRequestDto)
                .collect(Collectors.toList());
    }
}
