package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    Collection<Booking> findAllByItemIdAndStartAfterOrderByStartAsc(long itemId, LocalDateTime time);

    Collection<Booking> findAllByItemIdInAndStartAfterOrderByStartAsc(Collection<Long> itemIds,
                                                                      LocalDateTime time);

    Collection<Booking> findAllByBookerIdOrderByStartDesc(long bookerId);

    Collection<Booking> findAllByBookerIdAndEndBeforeOrderByStartDesc(long bookerId, LocalDateTime time);

    Optional<Booking> findByBookerIdAndItemIdAndEndBeforeOrderByStartDesc(long bookerId, long itemId,
                                                                          LocalDateTime time);

    Collection<Booking> findAllByBookerIdAndStartAfterOrderByStartDesc(long bookerId, LocalDateTime time);

    @Query("SELECT b from Booking AS b " +
            "WHERE b.booker.id = ?1 " +
            "AND ?2 > b.start " +
            "AND ?2 < b.end ")
    Collection<Booking> findAllCurrentBookings(long bookerId, LocalDateTime time);

    Collection<Booking> findAllByBookerIdAndStatusOrderByStartDesc(long bookerId, BookingStatus status);

    Collection<Booking> findAllByItemIdInAndStatusOrderByStartDesc(Collection<Long> itemIds,
                                                                   BookingStatus status);

    Collection<Booking> findAllByItemIdInOrderByStartDesc(Collection<Long> itemIds);

    Collection<Booking> findAllByItemIdInAndStartAfterOrderByStartDesc(Collection<Long> itemIds,
                                                                       LocalDateTime time);

    Collection<Booking> findAllByItemIdInAndEndBeforeOrderByStartDesc(Collection<Long> itemIds,
                                                                      LocalDateTime time);

    @Query("SELECT b from Booking AS b " +
            "WHERE b.item.id in ?1 " +
            "AND ?2 > b.start " +
            "AND ?2 < b.end ")
    Collection<Booking> findAllCurrentBookings(Collection<Long> itemIds, LocalDateTime time);
}
