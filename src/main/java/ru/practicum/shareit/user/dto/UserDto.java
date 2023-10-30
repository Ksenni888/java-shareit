package ru.practicum.shareit.user.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.UniqueElements;

import javax.validation.constraints.Email;

@Setter
@Getter
public class UserDto {
    private long id;

    private String name;

    @Email
    @UniqueElements
    private String email;

    public UserDto(String name, String email) {
    }
}
