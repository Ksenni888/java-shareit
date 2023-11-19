package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByBooker_id(long userId);

    List<Booking> findByItem_idIn(List<Long> allItemsByUser);

    List<Booking> findByItem_idAndStatus(long itemId, BookingStatus status);

    List<Booking> findByBooker_idAndItem_id(long userId, long itemId);
}