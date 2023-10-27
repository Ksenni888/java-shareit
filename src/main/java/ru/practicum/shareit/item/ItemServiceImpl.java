package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exeption.ObjectNotFoundException;
import ru.practicum.shareit.exeption.ValidException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private static final Logger log = LoggerFactory.getLogger(ItemServiceImpl.class);

    @Override
    public ItemDto create(long userId, ItemDto itemDto) {
        userCheck(userId);

        if (itemDto.getAvailable() == null) {
            log.warn("Available can't be empty");
            throw new ValidException("Available can't be empty");
        }
        if (itemDto.getName().isBlank()) {
            log.warn("Name can't be empty");
            throw new ValidException("Name can't be empty");
        }
        if (itemDto.getDescription() == null) {
            log.warn("Description can't be empty");
            throw new ValidException("Description can't be empty");
        }

        if (!userRepository.containsUser(userId)) {
            log.warn("This user is not exist");
            throw new ObjectNotFoundException("This user is not exist");
        }

        return itemRepository.create(userId, itemDto);
    }

    public ItemDto update(long userId, ItemDto itemDto, long itemId) {
        userCheck(userId);
        return itemRepository.update(userId, itemDto, itemId);
    }

    public ItemDto findById(long userId, long itemId) {
        return itemRepository.findById(userId, itemId);
    }

    public List<ItemDto> getItemsByUserId(long userId) {
        userCheck(userId);
        return itemRepository.getItemsByUserId(userId);
    }

    public List<ItemDto> findItems(long userId, String text) {
        return itemRepository.findItems(userId, text);
    }

    public void userCheck(long userId) {
        if (userId == 0) {
            log.warn("User id can't be empty");
            throw new ValidException("User id can't be empty");
        }
    }
}
