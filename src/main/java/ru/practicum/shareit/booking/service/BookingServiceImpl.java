package ru.practicum.shareit.booking.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.SavedBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collection;

@Service
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingMapper bookingMapper;

    @Override
    public BookingDto addBooking(long userId, SavedBookingDto savedBookingDto) {
        User user = getUserById(userId);
        long itemId = savedBookingDto.getItemId();
        Item item = getItemById(itemId);
        if (!item.isAvailable()) {
            throw new ValidationException(Item.class, "Не доступно для бронирования");
        }
        Booking booking = bookingMapper.map(savedBookingDto);
        if (booking.getStart().isAfter(booking.getEnd())) {
            throw new ValidationException(Booking.class, "Дата начала бронирования не может быть позже конца.");
        }
        booking.setBooker(user);
        booking.setItem(item);
        booking.setStatus(BookingStatus.WAITING);
        Booking savedBooking = bookingRepository.save(booking);

        return bookingMapper.map(savedBooking);
    }

    @Override
    public BookingDto manageBooking(long userId, long bookingId, boolean approved) {
        getUserById(userId);
        Booking booking = getBookingById(bookingId);
        if (booking.getItem().getOwner().getId() != userId) {
            throw new ValidationException(Booking.class, "Только владелец может управлять бронированием.");
        }
        booking.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        Booking savedBooking = bookingRepository.save(booking);
        return bookingMapper.map(savedBooking);
    }

    @Override
    public BookingDto getBooking(long userId, long bookingId) {
        getUserById(userId);
        Booking booking = getBookingById(bookingId);
        if (booking.getItem().getOwner().getId() != userId && booking.getBooker().getId() != userId) {
            throw new ValidationException(Booking.class, "Только владелец, или человек, оформивший бронирование " +
                    "может получить бронирование");
        }
        return bookingMapper.map(booking);
    }

    @Override
    public Collection<BookingDto> getAllUserBookings(long userId, BookingState state) {
        getUserById(userId);
        final Collection<Booking> bookings;
        final LocalDateTime current = LocalDateTime.now();
        switch (state) {
            case WAITING -> bookings = bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userId,
                    BookingStatus.WAITING);

            case REJECTED -> bookings = bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userId,
                    BookingStatus.REJECTED);

            case CURRENT -> bookings = bookingRepository.findAllCurrentBookings(userId, current);

            case PAST -> bookings = bookingRepository.findAllByBookerIdAndEndBeforeOrderByStartDesc(userId, current);

            case FUTURE -> bookings = bookingRepository.findAllByBookerIdAndStartAfterOrderByStartDesc(userId, current);

            case ALL -> bookings = bookingRepository.findAllByBookerIdOrderByStartDesc(userId);

            default -> throw new ValidationException(BookingState.class, "invalid");
        }
        return bookingMapper.map(bookings);
    }

    @Override
    public Collection<BookingDto> getAllUserItemsBookings(long userId, BookingState state) {
        getUserById(userId);
        Collection<Long> itemIds = itemRepository.findAllByOwnerId(userId).stream()
                .map(Item::getId)
                .toList();
        final Collection<Booking> bookings;
        final LocalDateTime current = LocalDateTime.now();
        switch (state) {

            case WAITING -> bookings = bookingRepository.findAllByItemIdInAndStatusOrderByStartDesc(itemIds,
                    BookingStatus.WAITING);

            case REJECTED -> bookings = bookingRepository.findAllByItemIdInAndStatusOrderByStartDesc(itemIds,
                    BookingStatus.REJECTED);

            case CURRENT -> bookings = bookingRepository.findAllCurrentBookings(itemIds, current);

            case PAST -> bookings = bookingRepository.findAllByItemIdInAndEndBeforeOrderByStartDesc(itemIds, current);

            case FUTURE -> bookings = bookingRepository.findAllByItemIdInAndStartAfterOrderByStartDesc(itemIds,
                    current);

            case ALL -> bookings = bookingRepository.findAllByItemIdInOrderByStartDesc(itemIds);

            default -> throw new ValidationException(BookingState.class, "invalid");
        }
        return bookingMapper.map(bookings);
    }

    private User getUserById(long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new AccessDeniedException("Пользователь с ID " + userId + " не найден"));
    }

    private Item getItemById(long itemId) {
        return itemRepository.findById(itemId).orElseThrow(
                () -> new NotFoundException("Предмет с ID " + itemId + " не найден"));
    }

    private Booking getBookingById(long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование  с ID " + bookingId + " не найдено"));
    }
}
