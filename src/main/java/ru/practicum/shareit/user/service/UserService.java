package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

public interface UserService {

    UserDto getById(Long userId);

    UserDto create(UserDto dto);

    UserDto update(UserDto dto, Long userId);

    void delete(Long userId);
}
