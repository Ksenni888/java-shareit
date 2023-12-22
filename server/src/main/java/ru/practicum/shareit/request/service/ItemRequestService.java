package ru.practicum.shareit.request.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto findRequestById(long userId, long requestId);

    ItemRequest addRequest(long userId, ItemRequestDto itemRequestDto);

    List<ItemRequestDto> getRequest(long userId);

    List<ItemRequestDto> getAllRequests(long userId, Pageable pageable);
}