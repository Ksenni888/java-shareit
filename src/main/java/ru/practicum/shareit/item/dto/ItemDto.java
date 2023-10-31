package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class ItemDto {

    private long id;

    private String name;

    private String description;

    private Boolean available;

    private long request;
}
