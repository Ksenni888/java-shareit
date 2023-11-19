package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserService;

@Component
@RequiredArgsConstructor
public class BookingMapper {
    private final UserService userService;
    private final ItemService itemService;
    private final ItemMapper itemMapper;
    private final UserMapper userMapper;

//    public Booking toBooking(Booking booking) {
//        return Booking.builder()
//                .id(booking.getId())
//                .start(booking.getStart())
//                .end(booking.getEnd())
//                .item(booking.getItem())
//                .booker(booking.getBooker())
//                .status(booking.getStatus())
//                .build();
//    }

//    public Booking toBooking(long userId, BookingDto bookingDto) {
//
//        return Booking.builder()
//                .id(bookingDto.getId())
//                .start(bookingDto.getStart())
//                .end(bookingDto.getEnd())
//               .item(
//                   //  itemMapper.toItemDto2(
//
//                       //bookingDto.getItemId() != 0 ? bookingDto.getItemId() : 0) //itemMapper.toItem(bookingDto.getItemId(), userId))
//                       itemService.findById(bookingDto.getItemId()))
//                .booker(
//                       userService.getById(userId))
//                .status(bookingDto.getStatus())
//                .build();
//    }
}