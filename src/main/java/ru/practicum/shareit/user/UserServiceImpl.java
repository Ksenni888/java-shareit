package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exeption.ExistExeption;
import ru.practicum.shareit.exeption.ObjectNotFoundException;
import ru.practicum.shareit.exeption.ValidException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public UserDto create(User user) {
        if (user.getId() != 0) {
            log.warn("id must be 0");
            throw new ValidException("id must be 0");
        }
        if (user.getEmail() == null) {
            throw new ValidException("Email can't by empty");
        }
        if (userRepository.existsByEmail(user)) {
            log.warn("User with email is exist");
            throw new ExistExeption("User with email is exist");
        }
        return userMapper.toUserDto(userRepository.create(user));
    }

    @Override
    public UserDto update(User user, long userId) {
        userContainsCheck(userId);
        if (userRepository.getAll().stream().anyMatch(x -> (x.getEmail().equals(user.getEmail())) && (userId != x.getId()))) {
            log.warn("User with email is exist");
            throw new ExistExeption("User with email is exist");
        }
        return userMapper.toUserDto(userRepository.update(user, userId));
    }

    @Override
    public List<UserDto> getAll() {
        return userRepository.getAll().stream().map(userMapper::toUserDto).collect(Collectors.toList());
    }

    @Override
    public UserDto getById(long userId) {
        userContainsCheck(userId);
        return userMapper.toUserDto(userRepository.getById(userId));
    }

    @Override
    public void deleteById(long userId) {
        userContainsCheck(userId);
        userRepository.deleteById(userId);
    }

    public void userContainsCheck(long userId) {
        if (!userRepository.containsUser(userId)) {
            log.warn("This user is not exist");
            throw new ObjectNotFoundException("This user is not exist");
        }
    }
}
