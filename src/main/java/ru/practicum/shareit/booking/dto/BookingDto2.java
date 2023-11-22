package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

    @Setter
    @Getter
    @Builder
    public class BookingDto2 {
        private long id;
        private LocalDateTime start;
        private LocalDateTime end;
        private Long bookerId;
    }