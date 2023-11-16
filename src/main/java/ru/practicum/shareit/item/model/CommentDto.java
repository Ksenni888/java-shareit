package ru.practicum.shareit.item.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
public class CommentDto {
    private long id;
    private String text;
    private long author;
    private String authorName;
    private LocalDateTime created;
}