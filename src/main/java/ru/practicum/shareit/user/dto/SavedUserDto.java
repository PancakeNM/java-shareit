package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.practicum.shareit.api.ValidateCreateRequest;
import ru.practicum.shareit.api.ValidateUpdateRequest;

@Data
public class SavedUserDto {

    @NotNull(message = "Имя не указано.", groups = {ValidateCreateRequest.class})
    @Size(min = 1, max = 50, message = "Имя должно быть длинной от 1 до 50 символов",
            groups = {ValidateCreateRequest.class})
    private String name;

    @NotNull(message = "Email не должен быть пустым", groups = {ValidateCreateRequest.class})
    @Email(message = "Неверный формат email.", groups = {ValidateCreateRequest.class, ValidateUpdateRequest.class})
    @Size(min = 1, max = 150, message = "Email должен быть длинной от 1 до 150 символов.",
    groups = {ValidateCreateRequest.class, ValidateUpdateRequest.class})
    private String email;
}
