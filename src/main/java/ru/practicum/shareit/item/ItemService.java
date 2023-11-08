package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    public ItemDto create(long userId, Item item);

    public ItemDto update(long userId, Item item, long itemId);

    public ItemDto findById(long itemId);

    public List<ItemDto> getItemsByUserId(long userId);

    public List<ItemDto> findItems(String text);
}
