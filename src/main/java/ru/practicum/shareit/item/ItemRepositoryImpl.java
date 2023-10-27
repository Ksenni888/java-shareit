package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exeption.ObjectNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ItemRepositoryImpl implements ItemRepository {
    private final Map<Long, ItemDto> itemsDto = new HashMap<>();
    private long itemId;
    private final ItemMapper itemMapper;
    private final UserRepository userService;

    public ItemDto create(long userId, ItemDto itemDto) {
        if (!userService.containsUser(userId)) {
            throw new ObjectNotFoundException("User not found");
        }
        itemDto.setId(incrementId());
        itemDto.setOwner(userService.getById(userId));
        itemsDto.put(itemDto.getId(), itemDto);
        return itemDto;
    }

    public ItemDto update(long userId, ItemDto itemDto, long itemId) {
        if (!itemsDto.containsKey(itemId)) {
            throw new ObjectNotFoundException("This item not found");
        }
        if (itemsDto.get(itemId).getOwner().getId() != userId) {
            throw new ObjectNotFoundException("Items can changes only owners");
        }
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
        if (!itemsDto.containsKey(itemId)) {
            throw new ObjectNotFoundException("Item not found");
        }
        return itemsDto.get(itemId);
    }

    public List<ItemDto> getItemsByUserId(long userId) {
        if (!userService.containsUser(userId)) {
            throw new ObjectNotFoundException("User not found");
        }
        return itemsDto.values().stream().filter(x -> x.getOwner().getId() == userId).collect(Collectors.toList());
    }

    public List<ItemDto> findItems(long userId, String text) {
        return itemsDto.values().stream().filter(x -> x.getName().toLowerCase().contains(text.toLowerCase()) || x.getDescription().toLowerCase().contains(text.toLowerCase()) && (x.getAvailable().equals(true))).collect(Collectors.toList());
    }

    public long incrementId() {
        return ++itemId;
    }
}
