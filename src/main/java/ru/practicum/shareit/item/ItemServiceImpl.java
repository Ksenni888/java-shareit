package ru.practicum.shareit.item;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ItemServiceImpl implements ItemService{
    private final Map<Long, ItemDto> items = new HashMap<>();

    @Override
    public Item create(Long userId, ItemDto itemDto) {
        items.put(userId,itemDto);
        return null;
    }
}
