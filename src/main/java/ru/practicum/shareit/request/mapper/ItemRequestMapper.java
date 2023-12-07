package ru.practicum.shareit.request.mapper;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemDtoForRequest;
import ru.practicum.shareit.request.dto.ItemRequestAll;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ItemRequestMapper {
    public ItemRequestDto toDtoRequest(ItemRequest itemRequest, List<Item> items) {
        return ItemRequestDto.builder()
                .id(itemRequest.getId())
                .created(itemRequest.getCreated())
                .description(itemRequest.getDescription())
                .items(items.stream().map(this::toDtoItem).collect(Collectors.toList()))
                .build();
    }

//    public ItemRequestAll toDtoRequestAll(ItemRequest itemRequest) {
//        return ItemRequestAll.builder()
//                .id(itemRequest.getId())
//                .created(itemRequest.getCreated())
//                .description(itemRequest.getDescription())
//                .build();
//
//    }

    public ItemDtoForRequest toDtoItem(Item item) {
        return ItemDtoForRequest.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .requestId(item.getRequest().getId())
                .available(item.getAvailable())
                .build();
    }

    public ItemRequest toRequest(ItemRequestDto itemRequestDto, User user) {
        return ItemRequest.builder()
                .id(itemRequestDto.getId())
                .created(LocalDateTime.now())
                .description(itemRequestDto.getDescription())
                .user(user)
                .build();
    }

}