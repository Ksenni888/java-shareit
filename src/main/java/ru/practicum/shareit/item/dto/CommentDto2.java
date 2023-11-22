package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

    @Setter
    @Getter
    @Builder
    public class CommentDto2 {
        private long id;
        private String text;
        private long author;
        private String authorName;
        private LocalDateTime created;
    }