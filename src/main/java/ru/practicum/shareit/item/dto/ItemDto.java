package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

@Data
public class ItemDto {
    private long id;

    private String name;

    private String description;

    private Status available;

    private User owner;

    private Item request;

    public ItemDto(String name, String description, boolean available, Long aLong) {
    }
}
