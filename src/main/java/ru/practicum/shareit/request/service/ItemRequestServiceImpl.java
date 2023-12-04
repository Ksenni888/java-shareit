package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exeption.ObjectNotFoundException;
import ru.practicum.shareit.exeption.ValidException;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.model.User;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemRequestServiceImpl implements ItemRequestService {

    private static final Logger log = LoggerFactory.getLogger(ItemRequestServiceImpl.class);
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final ItemRequestMapper itemRequestMapper;

    @Override
    public ItemRequestDto findRequestById(long userId, long requestId) {

        if (!userRepository.existsById(userId)) {
            throw new ObjectNotFoundException("User not found");
        }
        if (!itemRequestRepository.existsById(requestId)) {
            throw new ObjectNotFoundException("Request not found");
        }
        ItemRequest saveItemRequest = itemRequestRepository.findById(requestId).orElseThrow();
        return itemRequestMapper.toDtoRequest(saveItemRequest, itemRepository.findByRequestId(saveItemRequest.getId()));
    }

    @Override
    public ItemRequest addRequest(long userId, ItemRequestDto itemRequestDto) {

        if (itemRequestDto.getId() != 0) {
            log.warn("id must be 0");
            throw new ValidException("id must be 0");
        }

        if (!userRepository.existsById(userId)) {
            throw new ObjectNotFoundException("User not found");
        }

        User user = userRepository.findById(userId).orElseThrow();
        ItemRequest saveItemRequest = itemRequestMapper.toRequest(itemRequestDto, user);

        return itemRequestRepository.save(saveItemRequest);
    }

    @Override
    public List<ItemRequestDto> getRequest(long userId) {

        if (!userRepository.existsById(userId)) {
            throw new ObjectNotFoundException("User not found");
        }
        List<ItemRequest> saveItemRequests = itemRequestRepository.findByUserId(userId);
        if (saveItemRequests.isEmpty()) {
            return Collections.emptyList();
        }

        return saveItemRequests.stream().map(x -> itemRequestMapper.toDtoRequest(x, itemRepository.findByRequestId(x.getId())))
                .sorted(Comparator.comparing(ItemRequestDto::getCreated)).collect(Collectors.toList());
    }

    @Override
    public List<ItemRequestDto> getAllRequests(long userId, Pageable pageable) {

        return itemRequestRepository.findAll(pageable).stream()
                .filter(x -> x.getUser().getId() != userId)
                .map(x -> itemRequestMapper.toDtoRequest(x, itemRepository.findByRequestId(x.getId())))
                .collect(Collectors.toList());
    }
}