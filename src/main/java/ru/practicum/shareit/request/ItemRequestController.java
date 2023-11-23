package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private static final Logger log = LoggerFactory.getLogger(ItemRequestController.class);
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

    private final ItemRequestService itemRequestService;

    @GetMapping("/{requestId}")
    public ItemRequest findRequestById(@RequestHeader(USER_ID_HEADER) long userId, @PathVariable long requestId) {
        return itemRequestService.findRequestById(userId, requestId);
    }
}