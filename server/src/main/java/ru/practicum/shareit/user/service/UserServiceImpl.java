package ru.practicum.shareit.user.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public List<UserDto> getAll() {
        log.info("Получение списка всех пользователей");
        return userRepository.findAll().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getById(int id) {
        log.info("Запрос на получение пользователя с id {}", id);
        return UserMapper.toUserDto(userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException("Пользователь с таким id не найден")));
    }

    @Transactional
    @Override
    public UserDto create(UserDto userDto) {
        log.info("Добавлен пользователь {}.", userDto.getName());
        return UserMapper.toUserDto(userRepository.save(UserMapper.fromUserDto(userDto)));
    }

    @Transactional
    @Override
    public UserDto update(UserDto userDto, int id) {
        log.info("Обновляем пользователя c id {}", id);
        User user = userRepository.getReferenceById(id);
        User updatedUser = UserMapper.fromUserDto(userDto);
        updatedUser.setId(id);
        if (updatedUser.getName() == null) {
            updatedUser.setName(user.getName());
        }
        if (updatedUser.getEmail() == null) {
            updatedUser.setEmail(user.getEmail());
        }
        return UserMapper.toUserDto(userRepository.save(updatedUser));
    }

    @Transactional
    @Override
    public void delete(int id) {
        log.info("Удаляем пользователя с id {}", id);
        userRepository.deleteById(id);
    }
}
