package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.ExistExeption;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.exception.ValidException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserDto create(User user) {
        if (user.getId() != 0) {
            log.warn("id must be 0");
            throw new ValidException("id must be 0");
        }

        try {
            return userMapper.toDto(userRepository.save(user));
        } catch (Exception e) {
            throw new ExistExeption("Email can't be the same");
        }

    }

    @Override
    @Transactional
    public UserDto update(User user, long userId) {
        checkUserExists(userId);

        User baseUser = userRepository.findById(userId).orElseThrow();
        if (user.getEmail() != null) {
            baseUser.setEmail(user.getEmail());
        }

        if (user.getName() != null) {
            baseUser.setName(user.getName());
        }

        return userMapper.toDto(userRepository.save(baseUser));
    }

    @Override
    public List<UserDto> getAll() {
        return userRepository.findAll().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public User getById(long userId) {
        checkUserExists(userId);
        return userRepository.findById(userId).orElseThrow();
    }

    @Override
    @Transactional
    public void deleteById(long userId) {
        checkUserExists(userId);
        userRepository.deleteById(userId);
    }

    public void checkUserExists(long userId) {
        if (!userRepository.existsById(userId)) {
            log.warn("This user is not exist1");
            throw new ObjectNotFoundException("This user is not exist1");
        }
    }
}