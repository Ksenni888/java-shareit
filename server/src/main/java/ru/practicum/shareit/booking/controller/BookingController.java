package ru.practicum.shareit.booking.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@AllArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @GetMapping
    public List<BookingDto> getBookings(@RequestParam(defaultValue = "0") int from,
                                        @RequestParam(defaultValue = "20") int size,
                                        @RequestHeader("X-Sharer-User-Id") int userId,
                                        @RequestParam(required = false) String state) {
        return bookingService.getByUserId(from, size, userId, state);
    }

    @GetMapping("/owner")
    public List<BookingDto> getBookingsOwner(@RequestParam(defaultValue = "0") int from,
                                             @RequestParam(defaultValue = "20") int size,
                                             @RequestHeader("X-Sharer-User-Id") int userId,
                                             @RequestParam(required = false) String state) {
        return bookingService.getByOwnerId(from, size, userId, state);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBooking(@PathVariable int bookingId,
                                 @RequestHeader("X-Sharer-User-Id") int userId) {
        return bookingService.get(bookingId, userId);
    }

    @PostMapping
    public BookingDto addBooking(@Valid @RequestBody(required = false) BookingDto bookingDto,
                                 @RequestHeader("X-Sharer-User-Id") int userId) {
        return bookingService.create(bookingDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approvedBooking(@PathVariable int bookingId,
                                      @RequestHeader("X-Sharer-User-Id") int userId,
                                      @RequestParam Boolean approved) {
        return bookingService.approveBooking(bookingId, userId, approved);
    }
}
