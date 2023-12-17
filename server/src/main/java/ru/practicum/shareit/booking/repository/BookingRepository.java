package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public interface BookingRepository extends JpaRepository<Booking, Integer> {

    List<Booking> findAllByBookerIdOrderByStartDesc(Integer bookerId, Pageable pageable);

    List<Booking> findAllByBookerIdAndStatusOrderByStartDesc(Integer bookerId, Status status, Pageable pageable);

    List<Booking> findAllByBookerIdAndStartAfterOrderByStartDesc(Integer bookerId, LocalDateTime start, Pageable pageable);

    List<Booking> findAllByBookerIdAndEndBeforeOrderByStartDesc(Integer bookerId, LocalDateTime end, Pageable pageable);

    List<Booking> findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartAsc(Integer bookerId,
                                                                            LocalDateTime start,
                                                                            LocalDateTime end,
                                                                            Pageable pageable);

    List<Booking> findAllByItemOwnerIdOrderByStartDesc(Integer ownerId, Pageable pageable);

    List<Booking> findAllByItemOwnerIdAndStatusOrderByStartDesc(Integer ownerId, Status status, Pageable pageable);

    List<Booking> findAllByItemOwnerIdAndStartAfterOrderByStartDesc(Integer ownerId, LocalDateTime start, Pageable pageable);

    List<Booking> findAllByItemOwnerIdAndEndBeforeOrderByStartDesc(Integer ownerId, LocalDateTime end, Pageable pageable);

    List<Booking> findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(Integer ownerId,
                                                                                LocalDateTime start,
                                                                                LocalDateTime end,
                                                                                Pageable pageable);

    Optional<Booking> findFirstByItemIdAndStartBeforeAndStatusOrderByEndDesc(Integer itemId,
                                                                             LocalDateTime now,
                                                                             Status status);

    Optional<Booking> findFirstByItemIdAndStartAfterAndStatusOrderByStartAsc(Integer itemId,
                                                                             LocalDateTime now,
                                                                             Status status);

    Booking findFirstByItemIdAndBookerIdAndEndBeforeOrderByEndDesc(Integer itemId,
                                                                   Integer bookerId,
                                                                   LocalDateTime time);

    List<Booking> findByItemIn(List<Item> items);

}
