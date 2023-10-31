package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exeption.ObjectNotFoundException;
import ru.practicum.shareit.exeption.ValidException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private static final Logger log = LoggerFactory.getLogger(ItemServiceImpl.class);

    private final ItemRepository itemRepository;

    private final UserRepository userRepository;
    private final ItemMapper itemMapper;

    @Override
    public ItemDto create(long userId, Item item) {
        userCheck(userId);

        if (item.getAvailable() == null) {
            log.warn("Available can't be empty");
            throw new ValidException("Available can't be empty");
        }

        if (item.getName().isBlank()) {
            log.warn("Name can't be empty");
            throw new ValidException("Name can't be empty");
        }

        if (item.getDescription() == null) {
            log.warn("Description can't be empty");
            throw new ValidException("Description can't be empty");
        }

        if (!userRepository.containsUser(userId)) {
            log.warn("This user is not exist");
            throw new ObjectNotFoundException("This user is not exist");
        }

        return itemMapper.toItemDto(itemRepository.create(userId, item));
    }

    @Override
    public ItemDto update(long userId, Item item, long itemId) {
        userCheck(userId);

        if (!itemRepository.containsItem(itemId)) {
            throw new ObjectNotFoundException("This item not found");
        }

        if (itemRepository.findById(userId, itemId).getOwner().getId() != userId) {
            throw new ObjectNotFoundException("Items can changes only owners");
        }

        Item savedItem = itemRepository.findById(userId, itemId);
        if (item.getAvailable() != null) {
            savedItem.setAvailable(item.getAvailable());
        }

        if (item.getName() != null) {
            savedItem.setName(item.getName());
        }

        if (item.getDescription() != null) {
            savedItem.setDescription(item.getDescription());
        }

        return itemMapper.toItemDto(itemRepository.update(userId, savedItem, itemId));
    }

    @Override
    public ItemDto findById(long userId, long itemId) {
        if (itemRepository.findById(userId, itemId) == null) {
            throw new ObjectNotFoundException("Item not found");
        }
        return itemMapper.toItemDto(itemRepository.findById(userId, itemId));
    }

    @Override
    public List<ItemDto> getItemsByUserId(long userId) {
        userCheck(userId);
        if (userRepository.getById(userId) == null) {
            throw new ObjectNotFoundException("User not found");
        }
        return itemRepository.getItemsByUserId(userId).stream().map(itemMapper::toItemDto).collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> findItems(long userId, String text) {
        return itemRepository.findItems(userId, text).stream().map(itemMapper::toItemDto).collect(Collectors.toList());
    }

    public void userCheck(long userId) {
        if (userId == 0) {
            log.warn("User id can't be empty");
            throw new ValidException("User id can't be empty");
        }
    }
}
