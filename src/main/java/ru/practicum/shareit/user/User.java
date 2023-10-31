package ru.practicum.shareit.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;

@Getter
@Setter
@Builder
public class User {
    private long id;

    private String name;

    @Email
    private String email;
}
