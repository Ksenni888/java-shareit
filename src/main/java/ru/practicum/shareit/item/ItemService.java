package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDto2;
import ru.practicum.shareit.item.model.Comments;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    Item create(long userId, ItemDto itemDto);

    Item update(long userId, Item item, long itemId);

    ItemDto2 findById(long itemId, long userId);

    List<ItemDto2> getItemsByUserId(long userId);

    List<Item> findItems(String text);

    Comments addComment(long userId, long itemId, CommentDto commentDto);
}