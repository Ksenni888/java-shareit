package ru.practicum.shareit.booking;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class BookingRepositoryTest {
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookingRepository bookingRepository;

    private final Pageable pageable = PageRequest.of(0, 20, Sort.unsorted());

    @Test
    @DisplayName("Получение бронирования по id букера")
    void findAllByBookerIdOrderByStartDescTest() {
        List<Booking> listAll = createBooking();

        List<Booking> listFind = bookingRepository.findAllByBookerIdOrderByStartDesc(
                listAll.get(3).getBooker().getId(), pageable);

        assertEquals(listFind.size(), 3);
        assertEquals(listFind.get(0), listAll.get(5));
        assertEquals(listFind.get(1), listAll.get(4));
        assertEquals(listFind.get(2), listAll.get(3));
    }

    @Test
    @DisplayName("Получение бронирования по id букера до определенной даты")
    void findAllByBookerIdAndEndBeforeOrderByStartDescTest() {
        List<Booking> listAll = createBooking();

        List<Booking> listFind = bookingRepository.findAllByBookerIdAndEndBeforeOrderByStartDesc(
                listAll.get(3).getBooker().getId(), LocalDateTime.now().plusDays(7), pageable);

        assertEquals(listFind.size(), 3);
        assertEquals(listFind.get(0), listAll.get(5));
        assertEquals(listFind.get(1), listAll.get(4));
        assertEquals(listFind.get(2), listAll.get(3));

    }

    @Test
    @DisplayName("Получение бронирования по id букера после даты и окончанием до даты")
    void findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartAscTest() {
        List<Booking> listAll = createBooking();

        List<Booking> listFind = bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartAsc(
                listAll.get(3).getBooker().getId(),
                LocalDateTime.now().plusDays(5).plusMinutes(15),
                LocalDateTime.now().plusDays(5).plusMinutes(15),
                pageable);

        assertEquals(listFind.size(), 1);
        assertEquals(listFind.get(0), listAll.get(4));
    }

    @Test
    @DisplayName("Получение подтвержденного бронирования владельцем")
    void findAllByItemOwnerIdAndStatusApprovedOrderByStartDescTest() {
        List<Booking> listAll = createBooking();

        List<Booking> listFind = bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(
                listAll.get(0).getItem().getOwner().getId(),
                Status.APPROVED,
                pageable);

        assertEquals(listFind.size(), 2);
        assertEquals(listFind.get(0), listAll.get(3));
        assertEquals(listFind.get(1), listAll.get(1));
    }

    @Test
    @DisplayName("Получение бронирований в ожидании владельцем")
    void findAllByItemOwnerIdAndStatusWaitingOrderByStartDescTest() {
        List<Booking> listAll = createBooking();

        List<Booking> listFind = bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(
                listAll.get(0).getItem().getOwner().getId(),
                Status.WAITING,
                pageable);

        assertEquals(listFind.size(), 1);
        assertEquals(listFind.get(0), listAll.get(0));
    }

    @Test
    @DisplayName("Получение отказанных бронирований владельцем")
    void findAllByItemOwnerIdAndStatusRejectedOrderByStartDescTest() {
        List<Booking> listAll = createBooking();

        List<Booking> listFind = bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(
                listAll.get(0).getItem().getOwner().getId(),
                Status.REJECTED,
                pageable);

        assertEquals(listFind.size(), 1);
        assertEquals(listFind.get(0), listAll.get(4));
    }

    @Test
    @DisplayName("Получение бронирований владельцем до определенной даты")
    void findAllByItemOwnerIdAndEndBeforeOrderByStartDescTest() {
        List<Booking> listAll = createBooking();

        List<Booking> listFind = bookingRepository.findAllByItemOwnerIdAndEndBeforeOrderByStartDesc(
                listAll.get(0).getItem().getOwner().getId(),
                LocalDateTime.now().plusDays(4),
                pageable);

        assertEquals(listFind.size(), 2);
        assertEquals(listFind.get(0), listAll.get(1));
        assertEquals(listFind.get(1), listAll.get(0));
    }

    @Test
    @DisplayName("Получение бронирований владельцем после даты и до даты")
    void findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDescTest() {
        List<Booking> listAll = createBooking();

        List<Booking> listFind = bookingRepository.findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(
                listAll.get(0).getItem().getOwner().getId(),
                LocalDateTime.now().plusDays(1).plusMinutes(15),
                LocalDateTime.now().plusDays(1).plusMinutes(15),
                pageable);

        assertEquals(listFind.size(), 1);
        assertEquals(listFind.get(0), listAll.get(0));
    }


    private List<Booking> createBooking() {
        User user1 = new User(1, "user1", "user1@yandex.ru");
        User user2 = new User(2, "user2", "user2@yandex.ru");
        User user3 = new User(3, "user3", "user3@yandex.ru");
        user1 = userRepository.save(user1);
        user2 = userRepository.save(user2);
        user3 = userRepository.save(user3);

        Item item1 = new Item(1, "1", "1", true, user1, null);
        Item item2 = new Item(2, "2", "2", true, user1, null);
        Item item3 = new Item(3, "3", "3", true, user2, null);
        item1 = itemRepository.save(item1);
        item2 = itemRepository.save(item2);
        item3 = itemRepository.save(item3);

        Booking booking1 = new Booking(
                1,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                item1,
                user2,
                Status.WAITING
        );
        Booking booking2 = new Booking(
                2,
                LocalDateTime.now().plusDays(2),
                LocalDateTime.now().plusDays(3),
                item2,
                user2,
                Status.APPROVED
        );
        Booking booking3 = new Booking(
                3,
                LocalDateTime.now().plusDays(3),
                LocalDateTime.now().plusDays(4),
                item3,
                user1,
                Status.WAITING
        );
        Booking booking4 = new Booking(
                4,
                LocalDateTime.now().plusDays(4),
                LocalDateTime.now().plusDays(5),
                item2,
                user3,
                Status.APPROVED
        );
        Booking booking5 = new Booking(
                5,
                LocalDateTime.now().plusDays(5),
                LocalDateTime.now().plusDays(6),
                item1,
                user3,
                Status.REJECTED
        );
        Booking booking6 = new Booking(
                6,
                LocalDateTime.now().plusDays(6),
                LocalDateTime.now().plusDays(7),
                item3,
                user3,
                Status.WAITING
        );

        booking1 = bookingRepository.save(booking1);
        booking2 = bookingRepository.save(booking2);
        booking3 = bookingRepository.save(booking3);
        booking4 = bookingRepository.save(booking4);
        booking5 = bookingRepository.save(booking5);
        booking6 = bookingRepository.save(booking6);

        return List.of(booking1, booking2, booking3, booking4, booking5, booking6);


    }
}