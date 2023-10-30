package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ItemRepositoryImpl implements ItemRepository {
    private final Map<Long, ItemDto> itemsDto = new HashMap<>();

    private final UserService userService;
    private long itemId;

    public ItemDto create(long userId, ItemDto itemDto) {
        itemDto.setId(incrementId());
        itemDto.setOwner(userService.getById(userId));
        itemsDto.put(itemDto.getId(), itemDto);
        return itemDto;
    }

    public ItemDto update(long userId, ItemDto itemDto, long itemId) {

        if (itemDto.getAvailable() != null) {
            itemsDto.get(itemId).setAvailable(itemDto.getAvailable());
        }
        if (itemDto.getName() != null) {
            itemsDto.get(itemId).setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            itemsDto.get(itemId).setDescription(itemDto.getDescription());
        }
        return itemsDto.get(itemId);
    }

    public ItemDto findById(long userId, long itemId) {
        return itemsDto.get(itemId);
    }

    public List<ItemDto> getItemsByUserId(long userId) {
        return itemsDto.values().stream().filter(x -> x.getOwner().getId() == userId).collect(Collectors.toList());
    }

    public List<ItemDto> findItems(long userId, String text) {
        return itemsDto.values().stream().filter(x -> x.getName().toLowerCase().contains(text.toLowerCase()) || x.getDescription().toLowerCase().contains(text.toLowerCase()) && (x.getAvailable())).collect(Collectors.toList());
    }

    private long incrementId() {
        return ++itemId;
    }

    public boolean containsItem(long itemId) {
        return itemsDto.containsKey(itemId);
    }
}
