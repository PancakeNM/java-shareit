package ru.practicum.shareit.user.service;

import org.mapstruct.*;
import ru.practicum.shareit.user.dto.SavedUserDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Named("userToName")
    static String userToName(User user) {
        return user.getName();
    }

    User map(SavedUserDto savedUserDto);

    UserDto map(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserFromDto(@MappingTarget User user, SavedUserDto userDto);
}
