package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoForOwners;
import ru.practicum.shareit.item.model.Item;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private static final Logger log = LoggerFactory.getLogger(ItemController.class);
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

    private final ItemService itemService;

    private final ItemMapper itemMapper;

    @PostMapping
    public ItemDto create(@RequestHeader(USER_ID_HEADER) long userId, @Valid @RequestBody ItemDto itemDto) {
        log.info("Create item");
        return itemMapper.toItemDto(itemService.create(userId, itemDto));
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader(USER_ID_HEADER) long userId, @RequestBody Item item, @PathVariable long itemId) {
        log.info("Update item");

        return itemMapper.toItemDto(itemService.update(userId, item, itemId));
    }

    @GetMapping("/{itemId}")
    public ItemDtoForOwners findById(@RequestHeader(USER_ID_HEADER) long userId, @PathVariable long itemId) {
        log.info("Get information about item");
        return itemService.findById(itemId, userId);
    }

    @GetMapping
    public List<ItemDtoForOwners> getItemsByUserId(@RequestHeader(USER_ID_HEADER) long userId) {
        log.info("Get all user's items");
        return itemService.getItemsByUserId(userId);
    }

    @GetMapping("/search")
    @ResponseBody
    public List<ItemDto> findItems(@RequestHeader(USER_ID_HEADER) long userId, @RequestParam String text) {
        log.info("Seach items by request with available status");
        return itemService.findItems(text).stream()
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @PostMapping("/{itemId}/comment")
    @ResponseBody
    public CommentDto addComment(@RequestHeader(USER_ID_HEADER) long userId, @PathVariable long itemId, @RequestBody CommentDto commentDto) {
        return itemMapper.toCommentDto(itemService.addComment(userId, itemId, commentDto));
    }
}