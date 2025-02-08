package ru.practicum.shareit.item.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Collection;


@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemDto {
    long id;
    String name;
    String description;
    boolean available;
    LocalDateTime nextBooking;
    LocalDateTime lastBooking;
    Collection<CommentDto> comments;
}
