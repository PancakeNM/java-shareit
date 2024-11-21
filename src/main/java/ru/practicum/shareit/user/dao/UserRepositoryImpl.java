package ru.practicum.shareit.user.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.DataConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final Map<Long, User> users = new HashMap<>();
    private Long id = 0L;

    @Override
    public User getById(Long userId) {
        return findUser(userId);
    }

    @Override
    public User createNewUser(User newUser) {
        users.values().forEach(user -> {
                    if (user.getEmail().equals(newUser.getEmail())) {
                        log.error("This email is already registered");
                        throw new DataConflictException("Данный email уже зарегестрирован");
                    }
                });
        newUser.setId(generateNewId());
        users.put(newUser.getId(), newUser);
        return newUser;
    }

    @Override
    public User updateUser(User newUser, Long userId) {
        User oldUser = users.get(userId);
        userUpdater(oldUser, newUser);
        return oldUser;
    }

    @Override
    public void deleteUser(Long userId) {
        findUser(userId);
        users.remove(userId);
    }

    private void userUpdater(User oldUser, User newUser) {
        if (!oldUser.getName().equals(newUser.getName()) && newUser.getName() != null) {
            oldUser.setName(newUser.getName());
        }
        if (!oldUser.getEmail().equals(newUser.getEmail()) && newUser.getEmail() != null) {
            for(User user: users.values()) {
                if (user.getEmail().equals(newUser.getEmail()) && !user.getId().equals(oldUser.getId())) {
                    log.error("This email is already registered");
                    throw new DataConflictException("Данный email уже зарегестрирован");
                }
            }
            oldUser.setEmail(newUser.getEmail());
        }
    }

    private User findUser(Long id) {
        User user = users.get(id);
        if (user != null) {
            return user;
        } else {
            throw new NotFoundException(String.format("Пользователь с id = %s не найден", id));
        }
    }

    private Long generateNewId() {
        long oldId = id;
        id = id + 1;
        return oldId;
    }
}
