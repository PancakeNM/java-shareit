package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.SavedUserDto;
import ru.practicum.shareit.user.dto.UserDto;

public interface UserService {

    UserDto getUser(Long userId);

    UserDto createUser(SavedUserDto userDto);

    UserDto updateUser(SavedUserDto userDto, Long userId);

    void deleteUser(Long userId);
}
