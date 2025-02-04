package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dao.CommentRepository;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.SavedCommentDto;
import ru.practicum.shareit.item.dto.SavedItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepo;
    private final UserRepository userRepo;
    private final CommentRepository commentRepo;
    private final BookingRepository bookingRepo;

    private final ItemMapper itemMapper;
    private final CommentMapper commentMapper;

    @Transactional
    @Override
    public ItemDto addItem(long userId, SavedItemDto itemSaveDto) {
        User owner = userRepo.findById(userId).orElseThrow(
                () -> new NotFoundException("Пользователь с ID " + userId + " не найден"));
        Item item = itemMapper.map(itemSaveDto);
        item.setOwner(owner);
        Item savedItem = itemRepo.save(item);

        return itemMapper.map(savedItem);
    }

    @Override
    public CommentDto addComment(long userId, long itemId, SavedCommentDto commentSaveDto) {
        User owner = userRepo.findById(userId).orElseThrow(
                () -> new NotFoundException("Пользователь с ID " + userId + " не найден"));
        Item item = itemRepo.findById(itemId).orElseThrow(
                () -> new NotFoundException("Предмет с id " + itemId + " не найден"));
        Booking booking = bookingRepo
                .findByBookerIdAndItemIdAndEndBeforeOrderByStartDesc(userId, itemId, LocalDateTime.now())
                .orElseThrow(() -> new ValidationException(Comment.class, "Ошибка добавления комментария. " +
                       "Пользователь с id " + userId + "не бронировал предмет с id до этого " + itemId));

        Comment comment = commentMapper.map(commentSaveDto);
        comment.setAuthor(booking.getBooker());
        comment.setItem(booking.getItem());
        Comment savedComment = commentRepo.save(comment);

        return commentMapper.map(savedComment);
    }

    @Transactional
    @Override
    public ItemDto updateItem(long userId, long itemId, SavedItemDto itemSaveDto) {
        Item item = itemRepo.findById(itemId).orElseThrow(
                () -> new NotFoundException("Предмет с ID " + itemId + " не найден"));
        if (item.getOwner().getId() != userId) {
            throw new AccessDeniedException("Предмет может обновлять только его владелец");
        }
        itemMapper.updateItemFromDto(item, itemSaveDto);
        Item savedItem = itemRepo.save(item);

        return itemMapper.map(savedItem);
    }

    @Override
    public ItemDto getItem(long itemId) {
        Item item = itemRepo.findById(itemId).orElseThrow(
                () -> new NotFoundException("Предмет с ID " + itemId + " не найден"));
        ItemDto itemDto = itemMapper.map(item);

        Collection<LocalDateTime> futureBookings =
                bookingRepo.findAllByItemIdAndStartAfterOrderByStartAsc(itemId, LocalDateTime.now()).stream()
                        .map((Booking::getStart))
                        .toList();

        LocalDateTime nextBooking = findNextBooking(futureBookings);
        itemDto.setNextBooking(nextBooking);
        LocalDateTime lastBooking = findLastBooking(futureBookings);
        itemDto.setLastBooking(lastBooking);

        Collection<CommentDto> commentsDto = commentMapper.map(commentRepo.findAllByItemId(itemId));
        itemDto.setComments(commentsDto);

        return itemDto;
    }

    @Override
    public Collection<ItemDto> getAllOwnerItems(long userId) {
        Map<Long, ItemDto> itemIdToItemDto = itemRepo
                .findAllByOwnerId(userId)
                .stream()
                .map(itemMapper::map)
                .collect(Collectors.toMap(ItemDto::getId, Function.identity()));

        Map<Long, List<Booking>> itemIdToFutureBookings = bookingRepo
                .findAllByItemIdInAndStartAfterOrderByStartAsc(itemIdToItemDto.keySet(), LocalDateTime.now())
                .stream()
                .collect(Collectors.groupingBy(booking -> booking.getItem().getId(), Collectors.toList()));

        Map<Long, List<Comment>> itemIdToComments = commentRepo
                .findAllByItemIdIn(itemIdToItemDto.keySet()).stream()
                .collect(Collectors.groupingBy(comment -> comment.getItem().getId(), Collectors.toList()));

        for (ItemDto itemDto : itemIdToItemDto.values()) {
            if (!itemIdToFutureBookings.containsKey(itemDto.getId())) {
                continue;
            }
            Collection<LocalDateTime> futureItemBookings = itemIdToFutureBookings.get(itemDto.getId()).stream()
                    .map(Booking::getStart)
                    .toList();
            LocalDateTime nextBooking = findNextBooking(futureItemBookings);
            itemDto.setNextBooking(nextBooking);
            LocalDateTime lastBooking = findLastBooking(futureItemBookings);
            itemDto.setLastBooking(lastBooking);

            if (!itemIdToComments.containsKey(itemDto.getId())) {
                continue;
            }
            List<Comment> comments = itemIdToComments.get(itemDto.getId());
            itemDto.setComments(commentMapper.map(comments));
        }

        return itemIdToItemDto.values();
    }

    @Override
    public Collection<ItemDto> searchItems(String text) {
        if (text.isBlank()) {
            return List.of();
        }
        Collection<Item> items = itemRepo.searchItems(text);
        return itemMapper.map(items);
    }

    private LocalDateTime findNextBooking(Collection<LocalDateTime> futureBookings) {
        return futureBookings.stream()
                .reduce((first, last) -> first)
                .orElse(null);
    }

    private LocalDateTime findLastBooking(Collection<LocalDateTime> futureBookings) {
        return futureBookings.stream()
                .reduce((first, last) -> last)
                .orElse(null);
    }
}
