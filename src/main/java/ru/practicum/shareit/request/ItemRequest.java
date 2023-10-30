package ru.practicum.shareit.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ItemRequest {
    private long id;

    private String description;

    private Long requester;

    private LocalDate created;
}
