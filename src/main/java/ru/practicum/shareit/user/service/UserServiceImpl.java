package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserDtoMapper;
import ru.practicum.shareit.user.model.User;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserDtoMapper mapper;

    @Override
    public UserDto getById(Long userId) {
        return mapper.mapDtoFromUser(userRepository.getById(userId));
    }

    @Override
    public UserDto create(UserDto dto) {
        User newUser = mapper.mapUserFromDto(dto);
        return mapper.mapDtoFromUser(userRepository.createNewUser(newUser));
    }

    @Override
    public UserDto update(UserDto dto, Long userId) {
        User newUser = mapper.mapUserFromDto(dto);
        return mapper.mapDtoFromUser(userRepository.updateUser(newUser, userId));
    }

    @Override
    public void delete(Long userId) {
        userRepository.deleteUser(userId);
    }
}
