package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ItemRepositoryImpl implements ItemRepository {

    private final Map<Long, Item> items = new HashMap<>();

    private long itemId;

    @Override
    public Item create(long userId, Item item) {
        item.setId(incrementId());
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item update(long userId, Item item, long itemId) {

        if (item.getAvailable() != null) {
            items.get(itemId).setAvailable(item.getAvailable());
        }
        if (item.getName() != null) {
            items.get(itemId).setName(item.getName());
        }
        if (item.getDescription() != null) {
            items.get(itemId).setDescription(item.getDescription());
        }
        return items.get(itemId);
    }

    @Override
    public Item findById(long userId, long itemId) {
        return items.get(itemId);
    }

    @Override
    public List<Item> getItemsByUserId(long userId) {
        return items.values().stream().filter(x -> x.getOwner().getId() == userId).collect(Collectors.toList());
    }

    @Override
    public List<Item> findItems(long userId, String text) {
        return items.values().stream().filter(x -> x.getName().toLowerCase().contains(text.toLowerCase()) || x.getDescription().toLowerCase().contains(text.toLowerCase()) && (x.getAvailable())).collect(Collectors.toList());
    }

    @Override
    public boolean containsItem(long itemId) {
        return items.containsKey(itemId);
    }

    private long incrementId() {
        return ++itemId;
    }
}
