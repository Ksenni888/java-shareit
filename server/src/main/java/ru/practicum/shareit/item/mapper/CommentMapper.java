package ru.practicum.shareit.item.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

@UtilityClass
public class CommentMapper {
    public static CommentDto toDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getItem().getId(),
                comment.getAuthor().getName(),
                comment.getCreated(),
                comment.getText()
        );
    }

    public static Comment fromCommentDto(CommentDto commentDto, Item item, User user) {
        return new Comment(
                item,
                user,
                commentDto.getCreated(),
                commentDto.getText()
        );
    }
}
