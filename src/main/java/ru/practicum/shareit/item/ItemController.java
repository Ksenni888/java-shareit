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
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private static final Logger log = LoggerFactory.getLogger(ItemController.class);

    private final ItemService itemService;

    private final ItemMapper itemMapper;

    @PostMapping
    public ItemDto create(@RequestHeader("X-Sharer-User-Id") long userId, @RequestBody ItemDto itemDto) {
        log.info("Create item");
        return itemService.create(userId, itemMapper.toItem(itemDto, userId));
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader("X-Sharer-User-Id") long userId, @RequestBody ItemDto itemDto, @PathVariable long itemId) {
        log.info("Update item");
        return itemService.update(userId, itemMapper.toItem(itemDto, userId), itemId);
    }

    @GetMapping("/{itemId}")
    public ItemDto findById(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long itemId) {
        log.info("Get information about item");
        return itemService.findById(userId, itemId);
    }

    @GetMapping
    public List<ItemDto> getItemsByUserId(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Get all user's items");
        return itemService.getItemsByUserId(userId);
    }

    @GetMapping("/search")
    @ResponseBody
    public List<ItemDto> findItems(@RequestHeader("X-Sharer-User-Id") long userId, @RequestParam(defaultValue = "Write the text") String text) {
        log.info("Seach items by request with available status");
        return itemService.findItems(userId, text);
    }
}
