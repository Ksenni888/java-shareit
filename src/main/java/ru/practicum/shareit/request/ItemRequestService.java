package ru.practicum.shareit.request;

import org.springframework.web.bind.annotation.RequestHeader;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {
    public ItemRequest findRequestById(long userId, long requestId);

    public ItemRequest addRequest(long userId, ItemRequestDto itemRequestDto);

    List<ItemRequestDto> getRequest(long userId);
}