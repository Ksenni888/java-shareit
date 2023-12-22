package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.exception.ValidException;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";


    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(builder.uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new).build());
    }

    public ResponseEntity<Object> createBooking(long userId, BookingDto bookingDto) {
        LocalDateTime startTime = bookingDto.getStart();
        if (startTime.isAfter(bookingDto.getEnd())) {
            throw new ValidException("Data start can't be later then end");
        }
        if (startTime.isEqual(bookingDto.getEnd())) {
            throw new ValidException("Dates start and end can be different");
        }
        return post("", userId, bookingDto);
    }

    public ResponseEntity<Object> getBooking(long userId, long bookingId) {
        return get("/" + bookingId, userId);
    }

    public ResponseEntity<Object> checkRequest(long userId, long bookingId, String approved) {
        if (approved.isEmpty()) {
            throw new ValidException("approved must be true/false");
        }
        if (bookingId == 0) {
            throw new ValidException("bookingId can't be null");
        }
        return patch("/" + bookingId + "?approved=" + approved, userId);
    }

    public ResponseEntity<Object> getBookingsByStatus(long userId, String state, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "state", state,
                "from", from,
                "size", size);
        return get("?state={state}&from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity<Object> getUserBookings(long userId, String state, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "state", state,
                "from", from,
                "size", size);
        return get("/owner?state={state}&from={from}&size={size}", userId, parameters);
    }
}