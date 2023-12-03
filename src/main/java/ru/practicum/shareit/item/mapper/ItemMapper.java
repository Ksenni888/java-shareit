package ru.practicum.shareit.item.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.dto.BookingDto2;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoForOwners;
import ru.practicum.shareit.item.model.Comments;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ItemMapper {

    public ItemDto toItemDto(Item item) {

        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .requestId(item.getRequest() != null ? item.getRequest().getId() : 0)
                .build();
    }

    public Item toItem(ItemDto itemDto, User user, ItemRequest itemRequest) {
        return Item.builder()
                .id(itemDto.getId())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .owner(user)
                .request(itemDto.getRequestId() != 0 ? itemRequest : null).build();
    }

    public ItemDtoForOwners toItemDtoForOwners(Item item, long userId, Booking lastBooking, Booking nextBooking, List<CommentDto> comments) {

        return ItemDtoForOwners.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .request(item.getRequest() != null ? item.getRequest().getId() : 0)
                .lastBooking((lastBooking != null) && (item.getOwner().getId() == userId) ?
                        BookingDto2.builder()
                                .id(lastBooking.getId())
                                .start(lastBooking.getStart())
                                .end(lastBooking.getEnd())
                                .bookerId(lastBooking.getBooker().getId())
                                .build() : null
                )

                .nextBooking((nextBooking != null) && (item.getOwner().getId() == userId) ?
                        BookingDto2.builder()
                                .id(nextBooking.getId())
                                .start(nextBooking.getStart())
                                .end(nextBooking.getEnd())
                                .bookerId(nextBooking.getBooker().getId())
                                .build() : null
                )
                .comments(comments)
                .build();
    }

    public CommentDto toCommentDto(Comments comments) {
        return CommentDto.builder()
                .id(comments.getId())
                .author(comments.getAuthor().getId())
                .authorName(comments.getAuthor().getName())
                .text(comments.getText())
                .created(comments.getCreated())
                .build();
    }
}