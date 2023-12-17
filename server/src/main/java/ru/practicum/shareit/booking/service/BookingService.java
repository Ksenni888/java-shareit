package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

public interface BookingService {
    BookingDto get(int id, int userId);

    BookingDto create(BookingDto bookingDto, int userId);

    BookingDto approveBooking(int id, int userId, boolean approve);

    List<BookingDto> getByUserId(int from, int size, int userId, String state);

    List<BookingDto> getByOwnerId(int from, int size, int userId, String state);
}
