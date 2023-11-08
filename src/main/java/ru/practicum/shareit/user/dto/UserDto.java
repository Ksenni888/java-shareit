package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;

@Setter
@Getter
@Builder
public class UserDto {
    private long id;

    private String name;

    @Email
    private String email;
}
