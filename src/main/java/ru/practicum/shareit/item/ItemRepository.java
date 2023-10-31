package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {
    public Item create(long userId, Item item);

    public Item update(long userId, Item item, long itemId);

    public Item findById(long userId, long itemId);

    public List<Item> getItemsByUserId(long userId);

    public List<Item> findItems(long userId, String text);

    public boolean containsItem(long itemId);
}
