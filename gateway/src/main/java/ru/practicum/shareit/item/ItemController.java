package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.NotNullException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collections;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {

    private final ItemClient itemClient;

    @GetMapping
    public ResponseEntity<Object> getItems(@RequestHeader("X-Sharer-User-Id") long userId,
                                           @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                           @RequestParam(defaultValue = "20") @Positive Integer size) {
        log.info("getItems method, userId {}", userId);
        return itemClient.getItems(userId, from, size);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItem(@PathVariable long itemId,
                                          @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("getItem method, userId {}", userId);
        return itemClient.getItem(itemId, userId);
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader("X-Sharer-User-Id") long userId,
                                          @NotNull @Valid @RequestBody ItemDto itemDto) {
        log.info("createItem method, userId {}", userId);
        if (itemDto.getName().isBlank() || itemDto.getName() == null
                || itemDto.getDescription() == null) {
            throw new NotNullException("Поля имя, описание и доступность не могут быть пустыми");
        }
        if (itemDto.getAvailable() == null) {
            throw new NotNullException("Поле available не может быть пустым");
        }
        return itemClient.create(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(@PathVariable long itemId,
                                             @RequestHeader("X-Sharer-User-Id") long userId,
                                             @RequestBody ItemDto itemDto) {
        log.info("updateItem method, userId {}", userId);
        return itemClient.updateItem(itemId, userId, itemDto);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Object> delete(@PathVariable long itemId) {
        log.info("deleteItem method, itemId {}", itemId);
        return itemClient.delete(itemId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> search(@RequestParam String text,
                                              @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                              @RequestParam(defaultValue = "20") @Positive Integer size) {
        log.info("createItem method");
        if (text.isBlank()) return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);
        else return itemClient.search(text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@PathVariable long itemId,
                                             @Valid @RequestBody CommentDto commentDto,
                                             @RequestHeader("X-Sharer-User-Id") long authorId) {
        log.info("addComment method");
        if (commentDto.getText().isBlank()) {
            throw new ValidationException("Текст комментария не может быть пуст");
        }
        return itemClient.addComment(commentDto, itemId, authorId);
    }
}
