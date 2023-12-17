package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.ItemWithBookingDto;
import ru.practicum.shareit.item.dto.CommentDto;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ItemBookingDto {
    private int id;
    private String name;
    private String description;
    private int userId;
    private Boolean available;
    private ItemWithBookingDto lastBooking;
    private ItemWithBookingDto nextBooking;
    private List<CommentDto> comments;
}
