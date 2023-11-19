package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@Builder
public class ItemDto2 {

    private long id;

    private String name;

    private String description;

    private Boolean available;

    private long request;

    private Booking lastBooking;

    private Booking nextBooking;

    private List<Comment> comments;

    @Setter
    @Getter
    @Builder
    public static class Booking {
    private long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private Long bookerId;
   }

    @Setter
    @Getter
    @Builder
    public static class Comment {
       private long id;
       private String text;
       private long author;
       private String authorName;
       private LocalDateTime created;
   }
}