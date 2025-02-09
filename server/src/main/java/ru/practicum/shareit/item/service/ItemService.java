package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.SavedCommentDto;
import ru.practicum.shareit.item.dto.SavedItemDto;

import java.util.Collection;

public interface ItemService {
    ItemDto addItem(long userId, SavedItemDto savedItemDto);

    CommentDto addComment(long userId, long itemId, SavedCommentDto savedCommentDto);

    ItemDto updateItem(long userId, long itemId, SavedItemDto savedItemDto);

    ItemDto getItem(long itemId);

    Collection<ItemDto> getAllOwnerItems(long userId);

    Collection<ItemDto> searchItems(String text);
}
