package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private static final Logger log = LoggerFactory.getLogger(BookingController.class);
    private final String userIDhead = "X-Sharer-User-Id";

    private final BookingService bookingService;

    @PostMapping
    public Booking createBooking(@RequestHeader(userIDhead) long userId, @Valid @RequestBody BookingDto bookingDto) {
        log.info("Create booking");
        return bookingService.createBooking(userId, bookingDto);
    }

    @GetMapping("/{bookingId}")
    public Booking getBooking(@RequestHeader(userIDhead) long userId, @PathVariable long bookingId) {
        log.info("Get information by booking for owner item or booker only");
        return bookingService.getBooking(userId, bookingId);
    }

    @PatchMapping("/{bookingId}")
    public Booking checkRequest(@RequestHeader(userIDhead) long userId, @PathVariable long bookingId, @RequestParam String approved) {
        log.info("Check request booking");
        return bookingService.checkRequest(userId, bookingId, approved);
    }

    @GetMapping
    public List<Booking> getBookingsByStatus(@RequestHeader(userIDhead) long userId, @RequestParam(defaultValue = "ALL") String state) {
        log.info("Get list of user's bookings");
        return bookingService.getBookingsByStatus(userId, state);
    }

    @GetMapping("/owner")
    public List<Booking> getUserBookings(@RequestHeader(userIDhead) long userId, @RequestParam(defaultValue = "ALL") String state) {
        return bookingService.getUserBookings(userId, state);
    }
}
