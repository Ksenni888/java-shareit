package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemRepository {
    public ItemDto create(long userId, ItemDto itemDto);

    public ItemDto update(long userId, ItemDto itemDto, long itemId);

    public ItemDto findById(long userId, long itemId);

    public List<ItemDto> getItemsByUserId(long userId);

    public List<ItemDto> findItems(long userId, String text);
}
