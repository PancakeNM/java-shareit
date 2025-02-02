package ru.practicum.shareit.booking.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.practicum.shareit.booking.dto.SavedBookingDto;

import java.time.LocalDateTime;

public class DateTimeStartBeforeEndValidator implements ConstraintValidator<DateTimeStartBeforeEnd, SavedBookingDto> {

    @Override
    public void initialize(DateTimeStartBeforeEnd constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(SavedBookingDto savedBookingDto, ConstraintValidatorContext constraintValidatorContext) {
        LocalDateTime start = savedBookingDto.getStart();
        LocalDateTime end = savedBookingDto.getEnd();
        return start != null && end != null && !start.isAfter(end);
    }
}