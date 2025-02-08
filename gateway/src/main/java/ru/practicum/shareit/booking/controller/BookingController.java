package ru.practicum.shareit.booking.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.api.RequestHttpHeaders;
import ru.practicum.shareit.booking.client.BookingClient;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.booking.dto.SavedBookingDto;
import ru.practicum.shareit.exceptions.ValidationException;

@RestController
@RequestMapping(path = "/bookings")
@AllArgsConstructor
public class BookingController {
    private final BookingClient client;

    @PostMapping
    ResponseEntity<Object> addBooking(@RequestHeader(value = RequestHttpHeaders.USER_ID) long userId,
                                      @RequestBody @Valid SavedBookingDto savedBookingDto) {
        return client.addBooking(userId, savedBookingDto);
    }

    @PatchMapping("/{booking-id}")
    ResponseEntity<Object> manageBooking(@RequestHeader(value = RequestHttpHeaders.USER_ID) long userId,
                             @PathVariable(name = "booking-id") long bookingId, @RequestParam boolean approved) {
        return client.manageBooking(userId, bookingId, approved);
    }

    @GetMapping("/{booking-id}")
    ResponseEntity<Object> getBooking(@RequestHeader(value = RequestHttpHeaders.USER_ID) long userId,
                          @PathVariable(name = "booking-id") long bookingId) {
        return client.getBooking(userId, bookingId);
    }

    @GetMapping
    ResponseEntity<Object> getAllUserBookings(@RequestHeader(value = RequestHttpHeaders.USER_ID) long userId,
                                              @RequestParam(defaultValue = "all") String state,
                                              @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                              @RequestParam(defaultValue = "10") @Positive Integer size) {
        BookingState bookingState = getBookingState(state);
        return client.getAllUserBookings(userId, bookingState, from, size);
    }

    @GetMapping("/owner")
    ResponseEntity<Object> getAllUserItemsBookings(@RequestHeader(value = RequestHttpHeaders.USER_ID) long userId,
                                                   @RequestParam(defaultValue = "all") String state) {
        BookingState bookingState = getBookingState(state);
        return client.getAllUserItemsBookings(userId, bookingState);
    }

    private BookingState getBookingState(String state) {
        return BookingState.from(state)
                .orElseThrow(() -> new ValidationException(BookingState.class, state + " not valid"));
    }
}
