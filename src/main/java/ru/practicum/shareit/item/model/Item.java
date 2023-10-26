package ru.practicum.shareit.item.model;

import lombok.Data;
import ru.practicum.shareit.user.User;

@Data
public class Item {
    private long id;

    private String name;

    private String description;

    private boolean available;

    private User owner;

    private Item request;

}
