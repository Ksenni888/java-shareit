package ru.practicum.shareit.request;

import java.time.LocalDate;

import lombok.Data;
import ru.practicum.shareit.user.User;

/**
 * TODO Sprint add-item-requests.
 */
@Data
public class ItemRequest {
    private long id;

    private String description;

    private User requestor;

    private LocalDate created;
}
