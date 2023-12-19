package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
import ru.practicum.shareit.exception.ValidException;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
@Validated
public class BookingController {
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> createBooking(@RequestHeader(USER_ID_HEADER) long userId, @Valid @RequestBody BookingDto bookingDto) {
        LocalDateTime startTime = bookingDto.getStart();

        if (startTime.isAfter(bookingDto.getEnd())) {
            throw new ValidException("Data start can't be later then end");
        }

        if (startTime.isEqual(bookingDto.getEnd())) {
            throw new ValidException("Dates start and end can be different");
        }
        return bookingClient.createBooking(userId, bookingDto);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBooking(@RequestHeader(USER_ID_HEADER) long userId, @PathVariable long bookingId) {
        return bookingClient.getBooking(userId, bookingId);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> checkRequest(@RequestHeader(USER_ID_HEADER) long userId, @PathVariable long bookingId, @RequestParam String approved) {
        if (approved.isBlank()) {
            throw new ValidException("approved must be true/false");
        }

        if (bookingId == 0) {
            throw new ValidException("bookingId can't be null");
        }
        return bookingClient.checkRequest(userId, bookingId, approved);
    }

    @GetMapping
    public ResponseEntity<Object> getBookingsByStatus(@RequestHeader(USER_ID_HEADER) long userId, @RequestParam(defaultValue = "ALL") String state,
                                                      @RequestParam(defaultValue = "0") @Min(0) Integer from,
                                                      @RequestParam(defaultValue = "10") @Min(1) Integer size) {
        return bookingClient.getBookingsByStatus(userId, state, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getUserBookings(@RequestHeader(USER_ID_HEADER) long userId, @RequestParam(defaultValue = "ALL") String state,
                                                  @RequestParam(defaultValue = "0") @Min(0) Integer from,
                                                  @RequestParam(defaultValue = "10") @Min(1) Integer size) {
        return bookingClient.getUserBookings(userId, state, from, size);
    }
}