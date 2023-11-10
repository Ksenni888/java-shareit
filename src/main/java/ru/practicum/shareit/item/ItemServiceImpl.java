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

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private static final Logger log = LoggerFactory.getLogger(ItemServiceImpl.class);

    private final ItemRepository itemRepository;

    private final UserRepository userRepository;

    private final ItemMapper itemMapper;

    @Override
    public ItemDto create(long userId, Item item) {
        checkUser(userId);

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

        if (userRepository.existsById(userId)) {
            log.warn("This user is not exist");
            throw new ObjectNotFoundException("This user is not exist");
        }

        return itemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    public ItemDto update(long userId, Item item, long itemId) {
        checkUser(userId);

        if (!itemRepository.existsById(itemId)) {
            throw new ObjectNotFoundException("This item not found");
        }

        if (itemRepository.getReferenceById(itemId).getOwner().getId() != userId) {
            throw new ObjectNotFoundException("Items can changes only owners");
        }

        Item savedItem = itemRepository.getReferenceById(itemId);
        if (item.getAvailable() != null) {
            savedItem.setAvailable(item.getAvailable());
        }

        if (item.getName() != null) {
            savedItem.setName(item.getName());
        }

        if (item.getDescription() != null) {
            savedItem.setDescription(item.getDescription());
        }

        return itemMapper.toItemDto(itemRepository.save(savedItem));
    }

    @Override
    public ItemDto findById(long itemId) {
        if (!itemRepository.existsById(itemId)) {
            throw new ObjectNotFoundException("Item not found");
        }
        return itemMapper.toItemDto(itemRepository.getReferenceById(itemId));
    }

    @Override
    public List<ItemDto> getItemsByUserId(long userId) {
        checkUser(userId);
        if (!userRepository.existsById(userId)) {
            throw new ObjectNotFoundException("User not found");
        }
        return null;
//                itemRepository.findByUserId(userId).stream()
//                .map(itemMapper::toItemDto)
//                .collect(Collectors.toList());

    }

    @Override
    public List<ItemDto> findItems(String text) {
        if (text.isBlank()) {
            return Collections.emptyList();
        }
        return  null;
//                itemRepository.search(text).stream()
//                .map(itemMapper::toItemDto)
//                .collect(Collectors.toList());
    }

    public void checkUser(long userId) {
        if (userId == 0) {
            log.warn("User id can't be empty");
            throw new ValidException("User id can't be empty");
        }
    }
}
