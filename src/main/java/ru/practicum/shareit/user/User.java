package ru.practicum.shareit.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;

@Getter
@Setter
@NoArgsConstructor
public class User {
    private long id;

    private String name;

    @Email
    private String email;
}
