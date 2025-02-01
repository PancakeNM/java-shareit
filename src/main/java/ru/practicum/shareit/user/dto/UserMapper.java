package ru.practicum.shareit.user.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Named("userToName")
    static String userToName(User user) {
        return user.getName();
    }

    User map(SavedUserDto savedUserDto);
}
