package ru.practicum.shareit.user.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.api.ValidateCreateRequest;
import ru.practicum.shareit.api.ValidateUpdateRequest;
import ru.practicum.shareit.user.client.UserClient;
import ru.practicum.shareit.user.dto.SavedUserDto;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@AllArgsConstructor
@RequestMapping(path = "/users")
@Validated
public class UserController {
    private final UserClient client;

    @GetMapping("/{user-id}")
    public ResponseEntity<Object> getUser(@PathVariable("user-id") Long userId) {
        return client.getUser(userId);
    }

    @PostMapping
    public ResponseEntity<Object> createUser(@RequestBody @Validated(ValidateCreateRequest.class)SavedUserDto savedUserDto) {
        return client.createUser(savedUserDto);
    }

    @PatchMapping("/{user-id}")
    public ResponseEntity<Object> updateUser(@PathVariable("user-id") Long userId,
                          @RequestBody @Validated(ValidateUpdateRequest.class) SavedUserDto savedUserDto) {
        return client.updateUser(userId, savedUserDto);
    }

    @DeleteMapping("/{user-id}")
    public void deleteUser(@PathVariable("user-id") Long userId) {
        client.deleteUser(userId);
    }
}
