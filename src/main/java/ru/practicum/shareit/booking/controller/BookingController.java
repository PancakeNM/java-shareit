package ru.practicum.shareit.booking.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.api.RequestHttpHeaders;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.SavedBookingDto;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.Collection;


@RestController
@RequestMapping(path = "/bookings")
@AllArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    BookingDto addBooking(@RequestHeader(value = RequestHttpHeaders.USER_ID) long userId,
                          @RequestBody @Valid SavedBookingDto savedBookingDto) {
        return bookingService.addBooking(userId, savedBookingDto);
    }

    @PatchMapping("/{booking-id}")
    BookingDto manageBooking(@RequestHeader(value = RequestHttpHeaders.USER_ID) long userId,
                             @PathVariable(name = "booking-id") long bookingId, @RequestParam boolean approved) {
        return bookingService.manageBooking(userId, bookingId, approved);
    }

    @GetMapping("/{booking-id}")
    BookingDto getBooking(@RequestHeader(value = RequestHttpHeaders.USER_ID) long userId,
                          @PathVariable(name = "booking-id") long bookingId) {
        return bookingService.getBooking(userId, bookingId);
    }

    @GetMapping
    Collection<BookingDto> getAllUserBookings(@RequestHeader(value = RequestHttpHeaders.USER_ID) long userId,
                                              @RequestParam(defaultValue = "ALL") BookingState state) {
        return bookingService.getAllUserBookings(userId, state);
    }

    @GetMapping("/owner")
    Collection<BookingDto> getAllUserItemsBookings(@RequestHeader(value = RequestHttpHeaders.USER_ID) long userId,
                                                   @RequestParam(defaultValue = "ALL") BookingState state) {
        return bookingService.getAllUserItemsBookings(userId, state);
    }
}
