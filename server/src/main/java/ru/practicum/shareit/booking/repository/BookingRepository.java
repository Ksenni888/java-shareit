package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByBookerId(long userId, Pageable pageable);

    List<Booking> findByItemIdIn(List<Long> allItemsByUser, Pageable pageable);

    List<Booking> findByItemIdAndStatus(long itemId, BookingStatus status);

    List<Booking> findByBookerIdAndItemId(long userId, long itemId);
}