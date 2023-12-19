package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.exception.ValidException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.Item;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
@Validated
public class ItemController {
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> createItem(@RequestHeader(USER_ID_HEADER) long userId, @Valid @RequestBody ItemDto itemDto) {
        if (itemDto.getId() != 0) {
            throw new ValidException("id must be 0");
        }
        return itemClient.createItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestHeader(USER_ID_HEADER) long userId, @RequestBody Item item, @PathVariable long itemId) {
        return itemClient.updateItem(itemId, userId, item);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> findItemById(@PathVariable long itemId, @RequestHeader(USER_ID_HEADER) long userId) {
        return itemClient.findItemById(itemId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getItemsByUserId(@RequestHeader(USER_ID_HEADER) long userId) {
        return itemClient.getItemsByUserId(userId);
    }

    @GetMapping("/search")
    @ResponseBody
    public ResponseEntity<Object> findItems(@RequestHeader(USER_ID_HEADER) long userId, @RequestParam String text,
                                            @RequestParam(defaultValue = "0") @Min(0) Integer from,
                                            @RequestParam(defaultValue = "10") @Min(1) Integer size) {
//        if (text.isBlank()) {
//            throw new ValidationException("Text can't be empty");
//        }
        return itemClient.findItems(text, from, size, userId);
    }

    @PostMapping("/{itemId}/comment")
    @ResponseBody
    public ResponseEntity<Object> addComment(@RequestHeader(USER_ID_HEADER) long userId, @PathVariable long itemId, @RequestBody CommentDto commentDto) {
        if (commentDto.getText().isBlank()) {
            throw new ValidException("This field can't be empty, write the text");
        }
        return itemClient.addComment(userId, itemId, commentDto);
    }
}