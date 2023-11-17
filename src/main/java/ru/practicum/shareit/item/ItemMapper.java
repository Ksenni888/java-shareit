package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDto2;
import ru.practicum.shareit.item.model.CommentDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequestService;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ItemMapper {

    private final UserService userService;

    private final UserRepository userRepository;

    private final ItemRequestService itemRequestService;

    private final BookingRepository bookingRepository;

    private final CommentRepository commentRepository;

    public ItemDto toItemDto(Item item) {

        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                //     .request(item.getRequest() != null ? item.getRequest().getId() : 0)
                .build();
    }

    public Item toItem(ItemDto itemDto, long userId) {
        return Item.builder()
                .id(itemDto.getId())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .request(itemDto.getRequest() != 0 ? itemRequestService.findRequestById(itemDto.getRequest(), userId) : null)
                .owner(userService.getById(userId) != null ? userService.getById(userId) : null)
                .build();
    }

    public ItemDto2 toItemDto2(Item item, long userId) {

        Booking lastBooking = bookingRepository.findByItem_idAndStatus(item.getId(), BookingStatus.APPROVED).stream()
                .filter(x -> x.getEnd().isBefore(LocalDateTime.now()) || ((x.getStart().isBefore(LocalDateTime.now())) && (x.getEnd().isAfter(LocalDateTime.now()))))
                .max((Comparator.comparing(Booking::getEnd)))
                .orElse(null);
        Booking nextBooking = bookingRepository.findByItem_idAndStatus(item.getId(), BookingStatus.APPROVED).stream()
                .filter(x -> x.getStart().isAfter(LocalDateTime.now()))
                .min((Comparator.comparing(Booking::getStart)))
                .orElse(null);

        List<ItemDto2.Comment> comments = commentRepository.findByItem_id(item.getId()).stream()
                .map(x -> ItemDto2.Comment.builder()
                        .id(x.getId())
                        .author(x.getAuthor().getId())
                        .authorName(x.getAuthor().getName())
                        .text(x.getText())
                        .created(x.getCreated())
                        .build())
                .collect(Collectors.toList());

        return ItemDto2.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .request(item.getRequest() != null ? item.getRequest().getId() : 0)
                .lastBooking((lastBooking != null) && (item.getOwner().getId() == userId) ?
                        ItemDto2.Booking.builder()
                                .id(lastBooking.getId())
                                .start(lastBooking.getStart())
                                .end(lastBooking.getEnd())
                                .bookerId(lastBooking.getBooker().getId())
                                .build() : null
                )

                .nextBooking((nextBooking != null) && (item.getOwner().getId() == userId) ?
                        ItemDto2.Booking.builder()
                                .id(nextBooking.getId())
                                .start(nextBooking.getStart())
                                .end(nextBooking.getEnd())
                                .bookerId(nextBooking.getBooker().getId())
                                .build() : null
                )
                .comments(comments)
                .build();
    }

    public CommentDto toCommentDto(Item.Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .author(comment.getAuthor().getId())
                .authorName(userRepository.getReferenceById(comment.getAuthor().getId()).getName())
                .text(comment.getText())
                .created(comment.getCreated())
                .build();

    }
}
