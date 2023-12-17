package ru.practicum.shareit.booking.mapper;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.ItemWithBookingDto;
import ru.practicum.shareit.booking.model.Booker;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.ItemBooking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@UtilityClass
@Slf4j
public class BookingMapper {

    public static Booking fromBookingDto(BookingDto bookingDto, Item item, User booker) {
        return new Booking(
                bookingDto.getStart(),
                bookingDto.getEnd(),
                item,
                booker,
                Status.WAITING
        );
    }

    public static BookingDto toBookingDto(Booking booking) {
        return new BookingDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                new ItemBooking(booking.getItem().getId(), booking.getItem().getName()),
                new Booker(booking.getBooker().getId(), booking.getBooker().getName()),
                booking.getStatus(),
                booking.getItem().getId(),
                booking.getBooker().getId()
        );
    }

    public static List<BookingDto> toDtoList(List<Booking> booking) {
        return booking.stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    public static ItemWithBookingDto toItemWithBookingDto(Optional<Booking> booking) {
        return booking.map(value -> new ItemWithBookingDto(
                value.getId(),
                value.getBooker().getId()
        )).orElse(null);
    }
}