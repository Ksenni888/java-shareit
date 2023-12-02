package ru.practicum.shareit.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTests {

    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;

    public static User inputUser = User.builder()
            .id(0L)
            .name("Николай")
            .email("nik@mail.ru")
            .build();

    public static User inputUserAfterSave = User.builder()
            .id(1L)
            .name("Николай")
            .email("nik@mail.ru")
            .build();

    public static UserDto outputUserDto = UserDto.builder()
            .id(1L)
            .name("Николай")
            .email("nik@mail.ru")
            .build();

    public static User inputUserWithOtherName = User.builder()
            .id(0L)
            .name("Иван")
            .email("nik@mail.ru")
            .build();
    public static User UserWithOtherNameAfterSave = User.builder()
            .id(1L)
            .name("Иван")
            .email("nik@mail.ru")
            .build();

    public static UserDto UserWithOtherNameDto = UserDto.builder()
            .id(1L)
            .name("Иван")
            .email("nik@mail.ru")
            .build();

    @Test
    public void createUserTest() {

        Mockito.when(userRepository.save(inputUser)).thenReturn(inputUserAfterSave);
        Mockito.when(userMapper.toDto(inputUserAfterSave)).thenReturn(outputUserDto);

        UserDto result = userService.create(inputUser);

        Assertions.assertEquals(outputUserDto,result);
    }

  @Test
  public void updateUserTest() {

      Mockito.when(userRepository.existsById(1L)).thenReturn(true);
      Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(inputUserAfterSave));

      Mockito.when(userRepository.save(inputUserAfterSave)).thenReturn(UserWithOtherNameAfterSave);
      Mockito.when(userMapper.toDto(UserWithOtherNameAfterSave)).thenReturn(UserWithOtherNameDto);

      UserDto result = userService.update(inputUserWithOtherName, 1L);

      Assertions.assertEquals(UserWithOtherNameDto.getName(),result.getName());

  }



}
