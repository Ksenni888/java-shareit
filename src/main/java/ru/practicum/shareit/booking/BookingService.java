package ru.practicum.shareit.booking;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

public interface BookingService {
    Booking createBooking(long userId, BookingDto bookingDto);

    Booking checkRequest(long userId, long bookingId, String approved);

    Booking getBooking(long userId, long bookingId);

    List<Booking> getBookingsByStatus(long userId, String state, Pageable pageable);

    List<Booking> getUserBookings(long ownerId, String state, Pageable pageable);
}