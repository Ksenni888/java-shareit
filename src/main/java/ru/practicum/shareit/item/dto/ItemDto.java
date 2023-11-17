package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class ItemDto {

    private long id;

 //   @NotBlank
    private String name;

 //   @NotBlank
    private String description;

 //   @NotNull
    private Boolean available;

   // private long request;
}
