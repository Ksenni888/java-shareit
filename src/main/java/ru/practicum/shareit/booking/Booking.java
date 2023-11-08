package ru.practicum.shareit.booking;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDate;

@Getter
@Setter
public class Booking {
    private long id;

    private LocalDate start;

    private LocalDate end;

    private Item item;

    private User booker;

    private BookingStatus status;
}
