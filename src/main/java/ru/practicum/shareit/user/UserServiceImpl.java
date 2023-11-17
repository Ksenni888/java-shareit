package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exeption.ExistExeption;
import ru.practicum.shareit.exeption.ObjectNotFoundException;
import ru.practicum.shareit.exeption.ValidException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    @Transactional
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

        User saveUser = userRepository.findById(userId).orElseThrow();
        if (user.getEmail() != null) {
            saveUser.setEmail(user.getEmail());
        }

        if (user.getName() != null) {
            saveUser.setName(user.getName());
        }

        return userMapper.toDto(userRepository.save(saveUser));
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
