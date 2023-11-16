package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.exeption.ObjectNotFoundException;
import ru.practicum.shareit.exeption.ValidException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {
    private static final Logger log = LoggerFactory.getLogger(ItemServiceImpl.class);

    private final ItemRepository itemRepository;

    private final UserRepository userRepository;

    private final BookingRepository bookingRepository;

    private final CommentRepository commentRepository;

    @Override
    @Transactional
    public Item create(long userId, Item item) {
        checkUser(userId);

        if (null == item.getAvailable()) {
            log.warn("Available can't be empty");
            throw new ValidException("Available can't be empty");
        }

        if (item.getName().isBlank()) {
            log.warn("Name can't be empty");
            throw new ValidException("Name can't be empty");
        }

        if (null == item.getDescription()) {
            log.warn("Description can't be empty");
            throw new ValidException("Description can't be empty");
        }

        if (!userRepository.existsById(userId)) {
            log.warn("This user is not exist");
            throw new ValidException("This user is not exist");
        }

        return itemRepository.save(item);
    }

    @Override
    @Transactional
    public Item update(long userId, Item item, long itemId) {
        checkUser(userId);

        if (!itemRepository.existsById(itemId)) {
            throw new ObjectNotFoundException("This item not found");
        }

        Item savedItem = itemRepository.findById(itemId).orElseThrow();
        if (item.getAvailable() != null) {
            savedItem.setAvailable(item.getAvailable());
        }

        if (item.getName() != null) {
            savedItem.setName(item.getName());
        }

        if (item.getDescription() != null) {
            savedItem.setDescription(item.getDescription());
        }

        if (savedItem.getOwner().getId() != userId) {
            throw new ObjectNotFoundException("Items can changes only owners");
        }

        return itemRepository.save(savedItem);
    }

    @Override
    public Item findById(long itemId) {

        if (!itemRepository.existsById(itemId)) {
            throw new ObjectNotFoundException("Item not found");
        }
        return itemRepository.getReferenceById(itemId);
    }

    @Override
    public List<Item> getItemsByUserId(long userId) {
        checkUser(userId);
        if (!userRepository.existsById(userId)) {
            throw new ObjectNotFoundException("User not found");
        }

        return itemRepository.findByOwner_id(userId);
    }

    @Override
    public List<Item> findItems(String text) {
        if (text.isBlank()) {
            return Collections.emptyList();
        }
        return itemRepository.search(text).stream()
                .filter(Item::getAvailable)
                .collect(Collectors.toList());
    }

    @Override
    public Item.Comment addComment(long userId, long itemId, Item.Comment comment) {
        if (comment.getText().isBlank()) {
            throw new ValidException("This field can't be empty, write the text");
        }

        List<Booking> bookingsItemByUser = bookingRepository.findByBooker_idAndItem_id(userId, itemId);

        if (bookingsItemByUser.isEmpty()) {
            throw new ObjectNotFoundException("You can't write the comment, because you didn't booking this item");
        }

        List<Booking> bookingsBefore = bookingsItemByUser.stream()
                .filter(x -> x.getEnd().isBefore(LocalDateTime.now()))
                .collect(Collectors.toList());

        if (bookingsBefore.isEmpty()) {
            throw new ValidException("You can't comment, because you didn't use this item");
        }

        comment.setAuthor(userRepository.getReferenceById(userId));
        comment.setItem(itemRepository.getReferenceById(itemId));
        comment.setCreated(LocalDateTime.now());

        return commentRepository.save(comment);
    }

    public void checkUser(long userId) {
        if (userId == 0) {
            log.warn("User id can't be empty");
            throw new ValidException("User id can't be empty");
        }
    }

    public Booking findByItem_id(long itemId) {

        List<Booking> bookingByItemId = bookingRepository.findByItem_id(itemId)
                .stream()
                .filter(x -> x.getStatus().equals(BookingStatus.APPROVED))
                .sorted(Comparator.comparing(Booking::getStart))
                .collect(Collectors.toList());

        if (bookingByItemId.isEmpty()) {
            return null;
        } else if (bookingByItemId.size() < 2) {
            return bookingByItemId.get(0);
        }

        return bookingByItemId.get(0);
    }
}
