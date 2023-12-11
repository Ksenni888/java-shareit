package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exeption.ObjectNotFoundException;
import ru.practicum.shareit.exeption.ValidException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoForOwners;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comments;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

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
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ItemMapper itemMapper;

    @Override
    @Transactional
    public ItemDto create(long userId, ItemDto itemDto) {
        if (itemDto.getId() != 0) {
            log.warn("id must be 0");
            throw new ValidException("id must be 0");
        }

        if (!userRepository.existsById(userId)) {
            throw new ObjectNotFoundException("This user not found");
        }

        User user = userRepository.findById(userId).orElseThrow();

        Item item = itemMapper.toItem(itemDto, user, null);
        itemRequestRepository.findById(itemDto.getRequestId())
                .ifPresent(item::setRequest);

        return itemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    @Transactional
    public Item update(long userId, Item item, long itemId) {
        if (!itemRepository.existsById(itemId)) {
            throw new ObjectNotFoundException("This item not found");
        }

        Item baseItem = itemRepository.getReferenceById(itemId);

        if (item.getAvailable() != null) {
            baseItem.setAvailable(item.getAvailable());
        }

        if (item.getName() != null) {
            baseItem.setName(item.getName());
        }

        if (item.getDescription() != null) {
            baseItem.setDescription(item.getDescription());
        }

        if (baseItem.getOwner().getId() != userId) {
            throw new ObjectNotFoundException("Items can changes only owners");
        }

        return itemRepository.save(baseItem);
    }

    @Override
    public ItemDtoForOwners findById(long itemId, long userId) {
        if (!itemRepository.existsById(itemId)) {
            throw new ObjectNotFoundException("Item not found");
        }

        List<Booking> baseBookings = bookingRepository.findByItemIdAndStatus(itemId, BookingStatus.APPROVED);

        Booking lastBooking = baseBookings.stream()
                .filter(x -> x.getEnd().isBefore(LocalDateTime.now()) ||
                        ((x.getStart().isBefore(LocalDateTime.now())) && (x.getEnd().isAfter(LocalDateTime.now()))))
                .max((Comparator.comparing(Booking::getEnd))).orElse(null);

        Booking nextBooking = baseBookings.stream()
                .filter(x -> x.getStart().isAfter(LocalDateTime.now()))
                .min((Comparator.comparing(Booking::getStart))).orElse(null);

        List<CommentDto> comments = commentRepository.findByItemId(itemId).stream()
                .map(x -> CommentDto.builder()
                        .id(x.getId())
                        .author(x.getAuthor().getId())
                        .authorName(x.getAuthor().getName())
                        .text(x.getText())
                        .created(x.getCreated())
                        .build())
                .collect(Collectors.toList());

        return itemMapper.toItemDtoForOwners(itemRepository.getReferenceById(itemId), userId, lastBooking, nextBooking, comments);
    }

    @Override
    public List<ItemDtoForOwners> getItemsByUserId(long userId) {

        if (!userRepository.existsById(userId)) {
            throw new ObjectNotFoundException("User not found");
        }

        return itemRepository.findByOwnerId(userId).stream()
                .map(x -> findById(x.getId(), userId))
                .collect(Collectors.toList());
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
    public Comments addComment(long userId, long itemId, CommentDto commentDto) {
        if (commentDto.getText().isBlank()) {
            throw new ValidException("This field can't be empty, write the text");
        }

        List<Booking> bookingsItemByUser = bookingRepository.findByBookerIdAndItemId(userId, itemId);

        if (bookingsItemByUser.isEmpty()) {
            throw new ObjectNotFoundException("You can't write the comment, because you didn't booking this item");
        }

        List<Booking> bookingsEndsBeforeNow = bookingsItemByUser.stream()
                .filter(x -> x.getEnd().isBefore(LocalDateTime.now()))
                .collect(Collectors.toList());

        if (bookingsEndsBeforeNow.isEmpty()) {
            throw new ValidException("You can't comment, because you didn't use this item");
        }

        User user = userRepository.getReferenceById(userId);
        Item item = itemRepository.getReferenceById(itemId);
        Comments comments = itemMapper.toComment(commentDto, user, item);

        return commentRepository.save(comments);
    }
}