package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.user.UserService;

@Component
@RequiredArgsConstructor
public class BookingMapper {
    private final UserService userService;
    private final ItemService itemService;

    public BookingDto toDto(Booking booking) {
        return BookingDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .itemId(booking.getItem().getId())
                .status(booking.getStatus())
                .build();
    }

    public Booking toBooking(long userId, BookingDto bookingDto) {
        return Booking.builder()
                .id(bookingDto.getId())
                .start(bookingDto.getStart())
                .end(bookingDto.getEnd())
                .item(itemService.findById(bookingDto.getItemId()))
                .booker(userService.getById(userId))
                .status(bookingDto.getStatus())
                .build();
    }
}
