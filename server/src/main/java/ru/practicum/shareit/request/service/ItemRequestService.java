package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import java.util.List;

public interface ItemRequestService {
    ItemRequestDto addRequest(ItemRequestDto itemRequestDto, int requesterId);

    ItemRequestDto getRequestById(int requestId, int requesterId);

    List<ItemRequestDto> getRequestsByRequester(int requesterId);

    List<ItemRequestDto> getAllRequests(int requesterId, int from, int size);
}
