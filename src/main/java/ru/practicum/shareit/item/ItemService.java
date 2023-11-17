package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    Item create(long userId, Item item);

    Item update(long userId, Item item, long itemId);

    Item findById(long itemId);

    List<Item> getItemsByUserId(long userId);

    List<Item> findItems(String text);

    Item.Comment addComment(long userId, long itemId, Item.Comment comment);
}
