package ru.practicum.shareit.user.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.SavedUserDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserService service;

    @GetMapping("/{user-id}")
    public UserDto getUserById(@PathVariable("user-id") Long userId) {
        return service.getUser(userId);
    }

    @PostMapping
    public UserDto create(@RequestBody SavedUserDto savedUserDto) {
        return service.createUser(savedUserDto);
    }

    @PatchMapping("/{user-id}")
    public UserDto update(@PathVariable("user-id") Long userId,
                          @RequestBody SavedUserDto savedUserDto) {
        return service.updateUser(savedUserDto, userId);
    }

    @DeleteMapping("/{user-id}")
    public void delete(@PathVariable("user-id") Long userId) {
        service.deleteUser(userId);
    }
}
