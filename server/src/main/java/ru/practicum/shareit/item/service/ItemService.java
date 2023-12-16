package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoForOwners;
import ru.practicum.shareit.item.model.Comments;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    ItemDto create(long userId, ItemDto itemDto);

    Item update(long userId, Item item, long itemId);

    ItemDtoForOwners findById(long itemId, long userId);

    List<ItemDtoForOwners> getItemsByUserId(long userId);

    List<Item> findItems(String text);

    Comments addComment(long userId, long itemId, CommentDto commentDto);
}