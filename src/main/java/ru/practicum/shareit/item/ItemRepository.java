package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {
    public Item create(Item item);

    public Item update(Item item, long itemId);

    public Item findById(long itemId);

    public List<Item> getItemsByUserId(long userId);

    public List<Item> findItems(String text);

    public boolean containsItem(long itemId);
}
