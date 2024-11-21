package ru.practicum.shareit.user.dao;

import ru.practicum.shareit.user.model.User;

public interface UserRepository {

    User getById(Long userId);

    User createNewUser(User newUser);

    User updateUser(User newUser, Long userId);

    void deleteUser(Long userId);
}
