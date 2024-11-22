package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {

    private final UserService service;

    @GetMapping("/{user-id}")
    public UserDto getUserById(@PathVariable("user-id") Long userId) {
        return service.getById(userId);
    }

    @PostMapping
    public UserDto create(@RequestBody @Valid UserDto dto) {
        return service.create(dto);
    }

    @PatchMapping("/{user-id}")
    public UserDto update(@PathVariable("user-id") Long userId,
                          @RequestBody UserDto dto) {
        return service.update(dto, userId);
    }

    @DeleteMapping("/{user-id}")
    public void delete(@PathVariable("user-id") Long userId) {
        service.delete(userId);
    }
}
