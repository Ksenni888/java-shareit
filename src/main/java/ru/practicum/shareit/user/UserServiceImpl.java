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

        if (userRepository.existsByEmail(user.getEmail())) {
            log.warn("User with email is exist");
            throw new ExistExeption("User with email is exist");
        }

        return userMapper.toDto(userRepository.create(user));
    }

    @Override
    public UserDto update(User user, long userId) {
        checkUserExists(userId);
        if (userRepository.existByEmailAndId(user, userId)) {
            log.warn("User with email is exist");
            throw new ExistExeption("User with email is exist");
        }

        User saveUser = userRepository.getById(userId);
        if (user.getEmail() != null) {
            saveUser.setEmail(user.getEmail());
        }

        if (user.getName() != null) {
            saveUser.setName(user.getName());
        }

        return userMapper.toDto(userRepository.update(saveUser, userId));
    }

    @Override
    public List<UserDto> getAll() {
        return userRepository.getAll().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public User getById(long userId) {
        checkUserExists(userId);
        return userRepository.getById(userId);
    }

    @Override
    public void deleteById(long userId) {
        checkUserExists(userId);
        userRepository.deleteById(userId);
    }

    public void checkUserExists(long userId) {
        if (!userRepository.containsUser(userId)) {
            log.warn("This user is not exist");
            throw new ObjectNotFoundException("This user is not exist");
        }
    }
}
