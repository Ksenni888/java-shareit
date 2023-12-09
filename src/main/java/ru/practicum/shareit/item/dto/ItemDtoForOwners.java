package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.booking.dto.BookingDto2;

import java.util.List;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemDtoForOwners {

    private long id;

    private String name;

    private String description;

    private Boolean available;

    private long request;

    private BookingDto2 lastBooking;

    private BookingDto2 nextBooking;

    private List<CommentDto> comments;
}