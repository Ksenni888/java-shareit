package ru.practicum.shareit.item.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.DtoForNextLastBooking;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoForOwners;
import ru.practicum.shareit.item.model.Comments;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ItemMapper {

    public ItemDto toItemDto(Item item) {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable());
        itemDto.setRequestId(item.getRequest() != null ? item.getRequest().getId() : 0);
        return itemDto;
    }

    public Item toItem(ItemDto itemDto, User user, ItemRequest itemRequest) {
        Item item = new Item();
        item.setId(itemDto.getId());
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());
        item.setOwner(user);
        item.setRequest(itemRequest);
        return item;
    }

    public DtoForNextLastBooking createLastBooking(Booking lastBooking) {
        DtoForNextLastBooking dtoForNextLastBooking = new DtoForNextLastBooking();
        dtoForNextLastBooking.setId(lastBooking.getId());
        dtoForNextLastBooking.setStart(lastBooking.getStart());
        dtoForNextLastBooking.setEnd(lastBooking.getEnd());
        dtoForNextLastBooking.setBookerId(lastBooking.getBooker().getId());
        return dtoForNextLastBooking;
    }

    public DtoForNextLastBooking createNextBooking(Booking nextBooking) {
        DtoForNextLastBooking dtoForNextLastBooking = new DtoForNextLastBooking();
        dtoForNextLastBooking.setId(nextBooking.getId());
        dtoForNextLastBooking.setStart(nextBooking.getStart());
        dtoForNextLastBooking.setEnd(nextBooking.getEnd());
        dtoForNextLastBooking.setBookerId(nextBooking.getBooker().getId());
        return dtoForNextLastBooking;
    }

    public ItemDtoForOwners toItemDtoForOwners(Item item, long userId, Booking lastBooking, Booking nextBooking, List<CommentDto> comments) {
        ItemDtoForOwners itemDtoForOwners = new ItemDtoForOwners();
        itemDtoForOwners.setId(item.getId());
        itemDtoForOwners.setName(item.getName());
        itemDtoForOwners.setDescription(item.getDescription());
        itemDtoForOwners.setAvailable(item.getAvailable());
        itemDtoForOwners.setRequest(item.getRequest() != null ? item.getRequest().getId() : 0);
        itemDtoForOwners.setLastBooking((lastBooking != null) && (item.getOwner().getId() == userId) ? createLastBooking(lastBooking) : null);
        itemDtoForOwners.setNextBooking((nextBooking != null) && (item.getOwner().getId() == userId) ? createNextBooking(nextBooking) : null);
        itemDtoForOwners.setComments(comments);
        return itemDtoForOwners;
    }

    public CommentDto toCommentDto(Comments comments) {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(comments.getId());
        commentDto.setAuthor(comments.getAuthor().getId());
        commentDto.setAuthorName(comments.getAuthor().getName());
        commentDto.setText(comments.getText());
        commentDto.setCreated(comments.getCreated());
        return commentDto;
    }

    public Comments toComment(CommentDto commentDto, User user, Item item) {
        Comments comments = new Comments();
        comments.setId(commentDto.getId());
        comments.setText(commentDto.getText());
        comments.setItem(item);
        comments.setAuthor(user);
        comments.setCreated(LocalDateTime.now());
        return comments;
    }
}