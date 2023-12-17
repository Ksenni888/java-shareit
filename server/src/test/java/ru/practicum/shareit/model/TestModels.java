package ru.practicum.shareit.model;


import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.ItemWithBookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booker;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.ItemBooking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static ru.practicum.shareit.model.JsonModels.*;


public final class TestModels {
    public static final UserDto userDto = new UserDto(1, userName, email);
    public static final User user = new User(1, userName, email);
    public static final User booker = new User(2, userName2, email2);
    public static final ItemDto itemDto = new ItemDto(1, itemName, itemDescription, available, null);
    public static final ItemDto itemDtoUpd = new ItemDto(1, itemName2, itemDescription2, available, null);
    public static final Item item = new Item(1, itemName, itemDescription, available, user, null);
    public static final Item itemUpd = new Item(1, itemName2, itemDescription2, available, user, null);
    public static final Comment comment = new Comment(1, item, booker, created, text);
    public static final CommentDto commentDto = new CommentDto(1, item.getId(), userName2, created, text);
    public static final Booking last = new Booking(1,
            LocalDateTime.now().minusDays(2),
            LocalDateTime.now().minusDays(1),
            item,
            booker,
            Status.APPROVED);
    public static final Booking next = new Booking(1,
            start,
            end,
            item,
            booker,
            Status.APPROVED);
    public static final ItemWithBookingDto lastItemWithBooking = BookingMapper.toItemWithBookingDto(Optional.of(last));
    public static final ItemWithBookingDto nextItemWithBooking = BookingMapper.toItemWithBookingDto(Optional.of(next));
    public static final ItemBooking itemBooking = new ItemBooking(1, itemName);
    public static final Booker booker1 = new Booker(1, userName);
    public static final BookingDto bookingDto = new BookingDto(1, start, end, itemBooking, booker1, Status.WAITING, item.getId(), booker.getId());
    public static final ItemRequest itemRequest = new ItemRequest(1, text, user, created, new ArrayList<>());
    public static final ItemRequestDto itemRequestDto = ItemRequestMapper.toItemRequestDto(itemRequest);
}