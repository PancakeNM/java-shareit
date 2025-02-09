package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SavedCommentDto {
    @NotNull(message = "Комментарий не может быть пусты")
    @Size(min = 1, max = 300, message = "Комментарий должен быть от 1 до 300 символов")
    String text;
}
