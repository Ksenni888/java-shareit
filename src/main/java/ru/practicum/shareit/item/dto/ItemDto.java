package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

@Data
public class ItemDto {

    private long id;

    private String name;

    private String description;

    private Boolean available;

    private User owner;

    private ItemRequest request;

    public ItemDto(String name, String description, boolean available, Long aLong) {

    }

    public ItemDto() {

    }
}
