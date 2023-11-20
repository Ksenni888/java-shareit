package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByBookerId(long userId);

    List<Booking> findByItemIdIn(List<Long> allItemsByUser);

    List<Booking> findByItemIdAndStatus(long itemId, BookingStatus status);

    List<Booking> findByBookerIdAndItem_id(long userId, long itemId);
}