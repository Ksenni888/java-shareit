package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Validated
public class ItemRequestController {
    private static final Logger log = LoggerFactory.getLogger(ItemRequestController.class);
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

    private final ItemRequestClient itemRequestClient;

//    @GetMapping("/{requestId}")
//    public ItemRequestDto findRequestById(@RequestHeader(USER_ID_HEADER) long userId, @PathVariable long requestId) {
//        return itemRequestClient.findRequestById(userId, requestId);
//    }
//
//    @PostMapping
//    public ItemRequest addRequest(@RequestHeader(USER_ID_HEADER) long userId, @RequestBody @Valid ItemRequestDto itemRequestDto) {
//        return itemRequestClient.addRequest(userId, itemRequestDto);
//    }
//
//    @GetMapping
//    public List<ItemRequestDto> getRequest(@RequestHeader(USER_ID_HEADER) long userId) {
//
//        return itemRequestClient.getRequest(userId);
//    }
//
//    @GetMapping("/all")
//    public List<ItemRequestDto> getAllRequests(@RequestHeader(USER_ID_HEADER) long userId,
//                                               @RequestParam(defaultValue = "0") @Min(0) Integer from,
//                                               @RequestParam(defaultValue = "10") @Min(1) Integer size) {
//
//        return itemRequestClient.getAllRequests(userId, PageRequest.of(from / size, size, Sort.by("created").descending()));
//    }
}