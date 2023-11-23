package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

public interface BookingService {
    Booking createBooking(long userId, BookingDto bookingDto);

    Booking checkRequest(long userId, long bookingId, String approved);

    Booking getBooking(long userId, long bookingId);

    List<Booking> getBookingsByStatus(long userId, String state);

    List<Booking> getUserBookings(long ownerId, String state);
}