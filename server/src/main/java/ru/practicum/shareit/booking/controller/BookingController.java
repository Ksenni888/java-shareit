package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
@Validated
public class BookingController {
    private static final Logger log = LoggerFactory.getLogger(BookingController.class);
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

    private final BookingService bookingService;

    @PostMapping
    public Booking createBooking(@RequestHeader(USER_ID_HEADER) long userId, @Valid @RequestBody BookingDto bookingDto) {
        log.info("Create booking");
        return bookingService.createBooking(userId, bookingDto);
    }

    @GetMapping("/{bookingId}")
    public Booking getBooking(@RequestHeader(USER_ID_HEADER) long userId, @PathVariable long bookingId) {
        log.info("Get information by booking for owner item or booker only");
        return bookingService.getBooking(userId, bookingId);
    }

    @PatchMapping("/{bookingId}")
    public Booking checkRequest(@RequestHeader(USER_ID_HEADER) long userId, @PathVariable long bookingId, @RequestParam String approved) {
        log.info("Check request booking");
        return bookingService.checkRequest(userId, bookingId, approved);
    }

    @GetMapping
    public List<Booking> getBookingsByStatus(@RequestHeader(USER_ID_HEADER) long userId,
                                             @RequestParam(defaultValue = "ALL") String state,
                                             @RequestParam(defaultValue = "0") @Min(0) Integer from,
                                             @RequestParam(defaultValue = "10") @Min(1) Integer size) {

        log.info("Get list of user's bookings");
        return bookingService.getBookingsByStatus(userId, state,
                PageRequest.of(from / size, size, Sort.by("start").descending()));
    }

    @GetMapping("/owner")
    public List<Booking> getUserBookings(@RequestHeader(USER_ID_HEADER) long userId,
                                         @RequestParam(defaultValue = "ALL") String state,
                                         @RequestParam(defaultValue = "0") @Min(0) Integer from,
                                         @RequestParam(defaultValue = "10") @Min(1) Integer size) {

        return bookingService.getUserBookings(userId, state,
                PageRequest.of(from / size, size, Sort.by("start").descending()));
    }
}