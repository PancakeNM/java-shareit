package ru.practicum.shareit.exceptions;

public class ValidationException extends RuntimeException {

    public ValidationException(Class<?> entity, String reason) {
        super(entity.getSimpleName() + " " + reason);
    }
}
