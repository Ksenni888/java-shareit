package ru.practicum.shareit.item;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

@RestController
@RequestMapping("/items")
public class ItemController {
    private ItemService itemService;

    @PostMapping
public Item create(@RequestHeader("X-Later-User-Id") Long userId,
                   @RequestBody ItemDto itemDto){
return itemService.create(userId, itemDto);
    }
  //  POST /items На вход поступает объект ItemDto.
    //  userId в заголовке X-Sharer-User-Id — это идентификатор пользователя, который добавляет вещь.
    //  Именно этот пользователь — владелец вещи. Идентификатор владельца будет поступать на вход в каждом из запросов, рассмотренных далее.


}
