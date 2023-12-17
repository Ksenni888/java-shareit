package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.ItemWithBookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemBookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserService userService;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ItemRequestRepository itemRequestRepository;

    @Override
    public List<ItemBookingDto> getAllByOwner(int ownerId, int from, int size) {
        log.info("Запрос всех вещей пользователя {}", ownerId);
        userService.getById(ownerId);
        Pageable pageable = PageRequest.of(from / size, size, Sort.by(Sort.Direction.ASC, "id"));
        return mapToItemWithBooking(itemRepository.findByOwnerId(ownerId, pageable));
    }

    @Override
    public Item getById(int id) {
        if (itemRepository.findById(id) == null) {
            throw new UserNotFoundException(String.format("Вещь с id %d не найдена", id));
        } else return itemRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemDto> search(String text, int from, int size) {
        if (text.isBlank()) return new ArrayList<>();
        else {
            Pageable pageable = PageRequest.of(from, size, Sort.unsorted());
            return itemRepository.findItemByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(text, text, pageable).stream()
                    .filter(Item::getAvailable)
                    .map(ItemMapper::toItemDto)
                    .collect(toList());
        }
    }

    @Override
    @Transactional
    public ItemDto create(ItemDto itemDto, int ownerId) {
        log.info("Запрос на создание вещи");
        User user = UserMapper.fromUserDto(userService.getById(ownerId));
        ItemRequest itemRequest = itemDto.getRequestId() != null ?
                itemRequestRepository.findById(
                        itemDto.getRequestId()).orElseThrow(
                        () -> new UserNotFoundException("Такого запроса нет")) : null;
        return ItemMapper.toItemDto(itemRepository.save(ItemMapper.fromItemDto(itemDto, user, itemRequest)));
    }

    @Override
    @Transactional
    public ItemDto update(ItemDto itemDto, int ownerId, int id) {
        log.info("Запрос на обновление вещи с id {}", id);
        Item item = itemRepository.getReferenceById(id);
        if (item.getOwner().getId() != ownerId) {
            throw new UserNotFoundException("Вещь может обновить только владелец");
        }
        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }
        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    @Transactional(readOnly = true)
    public ItemBookingDto getItem(int itemId, int userId) {
        log.info("Запрос вещи с id {}", itemId);
        try {
            Item item = itemRepository.findById(itemId);
            ItemWithBookingDto last = null;
            ItemWithBookingDto next = null;
            List<Comment> comments = commentRepository.findAllByItemId(item.getId());
            if (item.getOwner().getId() == userId) {
                last = BookingMapper.toItemWithBookingDto(bookingRepository
                        .findFirstByItemIdAndStartBeforeAndStatusOrderByEndDesc(item.getId(), LocalDateTime.now(), Status.APPROVED));
                next = BookingMapper.toItemWithBookingDto(bookingRepository
                        .findFirstByItemIdAndStartAfterAndStatusOrderByStartAsc(item.getId(), LocalDateTime.now(), Status.APPROVED));
            }
            return ItemMapper.toItemBookingDto(
                    item,
                    last,
                    next,
                    comments.stream().map(CommentMapper::toDto).collect(toList())
            );
        } catch (NullPointerException e) {
            throw new UserNotFoundException("Вещи с таким id не существует");
        }
    }

    @Override
    @Transactional
    public CommentDto addComment(int itemId, CommentDto commentDto, int authorId) {
        Booking booking = bookingRepository.findFirstByItemIdAndBookerIdAndEndBeforeOrderByEndDesc(
                itemId,
                authorId,
                LocalDateTime.now()
        );

        if (booking == null) {
            throw new ValidationException("Вещь должна быть забронирована");
        }
        Comment comment = CommentMapper.fromCommentDto(commentDto, booking.getItem(), booking.getBooker());
        return CommentMapper.toDto(commentRepository.save(comment));
    }

    @Override
    @Transactional
    public void delete(int id) {
        log.info("Запрос на удаление вещи с id {}", id);
        itemRepository.deleteById(id);
    }

    private List<ItemBookingDto> mapToItemWithBooking(List<Item> items) {
        Map<Item, List<Comment>> comments = commentRepository.findByItemIn(items, Sort.by(Sort.Direction.DESC, "created"))
                .stream().collect((groupingBy(Comment::getItem, toList())));
        Map<Item, List<Booking>> approvedBookings = bookingRepository.findByItemIn(items)
                .stream().collect(groupingBy(Booking::getItem, toList()));
        List<ItemBookingDto> itemBookingDtoList = new ArrayList<>();
        for (Item item : items) {
            ItemWithBookingDto lastBooking = BookingMapper.toItemWithBookingDto(
                    approvedBookings.getOrDefault(item, new ArrayList<>())
                            .stream()
                            .filter(booking -> booking.getStart().isBefore(LocalDateTime.now()))
                            .filter(booking -> booking.getStatus() == Status.APPROVED)
                            .max(Comparator.comparing(Booking::getStart)));
            ItemWithBookingDto nextBooking = BookingMapper.toItemWithBookingDto(
                    approvedBookings.getOrDefault(item, new ArrayList<>())
                            .stream()
                            .filter(booking -> booking.getStart().isAfter(LocalDateTime.now()))
                            .filter(booking -> booking.getStatus() == Status.APPROVED)
                            .min(Comparator.comparing(Booking::getStart)));
            List<CommentDto> commentDtoList = comments.getOrDefault(item, new ArrayList<>())
                    .stream()
                    .map(CommentMapper::toDto)
                    .collect(toList());
            itemBookingDtoList.add(ItemMapper.toItemBookingDto(item, lastBooking, nextBooking, commentDtoList));
        }
        return itemBookingDtoList;
    }
}
