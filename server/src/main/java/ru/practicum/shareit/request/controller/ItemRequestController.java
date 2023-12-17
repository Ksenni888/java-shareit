package ru.practicum.shareit.request.controller;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;


@Validated
@RestController
@RequestMapping(path = "/requests")
@AllArgsConstructor
public class ItemRequestController {

    ItemRequestService itemRequestService;

    @GetMapping("/all")
    public List<ItemRequestDto> getItemRequestsAll(@RequestParam(defaultValue = "0") @Min(0) int from,
                                                   @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size,
                                                   @RequestHeader("X-Sharer-User-Id") int requesterId) {
        return itemRequestService.getAllRequests(requesterId, from, size);
    }

    @GetMapping
    public List<ItemRequestDto> getItemRequestsForRequester(@RequestHeader("X-Sharer-User-Id") int requesterId) {
        return itemRequestService.getRequestsByRequester(requesterId);
    }

    @GetMapping("/{id}")
    public ItemRequestDto getItemRequest(@PathVariable(name = "id") int itemRequestId,
                                         @RequestHeader("X-Sharer-User-Id") int requesterId) {
        return itemRequestService.getRequestById(itemRequestId, requesterId);
    }

    @PostMapping
    public ItemRequestDto addItemRequest(@Valid @RequestBody ItemRequestDto itemRequestDto,
                                         @RequestHeader("X-Sharer-User-Id") int requesterId) {
        return itemRequestService.addRequest(itemRequestDto, requesterId);
    }
}
