package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;


@Validated
@RestController
@RequestMapping(path = "/requests")
@AllArgsConstructor
public class ItemRequestController {

    ItemRequestClient itemRequestClient;

    @GetMapping("/all")
    public ResponseEntity<Object> getItemRequestsAll(@RequestParam(defaultValue = "0") @Min(0) Integer from,
                                                     @RequestParam(defaultValue = "20") @Min(1) @Max(100) Integer size,
                                                     @RequestHeader("X-Sharer-User-Id") long requesterId) {
        return itemRequestClient.getAllRequests(from, size, requesterId);
    }

    @GetMapping
    public ResponseEntity<Object> getItemRequestsForRequester(@RequestHeader("X-Sharer-User-Id") long requesterId) {
        return itemRequestClient.getItemRequestsByRequester(requesterId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getItemRequest(@PathVariable(name = "id") long itemRequestId,
                                         @RequestHeader("X-Sharer-User-Id") long requesterId) {
        return itemRequestClient.getRequestById(itemRequestId, requesterId);
    }

    @PostMapping
    public ResponseEntity<Object> addItemRequest(@Valid @RequestBody ItemRequestDto itemRequestDto,
                                         @RequestHeader("X-Sharer-User-Id") long requesterId) {
        return itemRequestClient.addRequest(itemRequestDto, requesterId);
    }
}