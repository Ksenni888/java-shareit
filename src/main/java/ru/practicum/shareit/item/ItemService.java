package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    ItemDto create(long userId, Item item);

    ItemDto update(long userId, Item item, long itemId);

    ItemDto findById(long itemId);

    List<ItemDto> getItemsByUserId(long userId);

    List<ItemDto> findItems(String text);
}
