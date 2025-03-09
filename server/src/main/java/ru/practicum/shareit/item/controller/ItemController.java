package ru.practicum.shareit.item.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.api.RequestHttpHeaders;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.SavedCommentDto;
import ru.practicum.shareit.item.dto.SavedItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.Collection;

@RestController
@AllArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemService service;

    @GetMapping("/{item-id}")
    public ItemDto getItem(@PathVariable("item-id") long itemId) {
        return service.getItem(itemId);
    }

    @PostMapping
    public ItemDto addItem(@RequestBody SavedItemDto itemDto,
                           @RequestHeader(value = RequestHttpHeaders.USER_ID) long userId) {
        return service.addItem(userId, itemDto);
    }

    @PostMapping("/{item-id}/comment")
    public CommentDto addComment(@RequestHeader(value = RequestHttpHeaders.USER_ID) long userId,
                          @PathVariable("item-id") long itemId,
                          @RequestBody SavedCommentDto savedCommentDto) {
        return service.addComment(userId, itemId, savedCommentDto);
    }

    @PatchMapping("/{item-id}")
    public ItemDto updateItem(@RequestBody SavedItemDto itemDto,
                              @RequestHeader(value = RequestHttpHeaders.USER_ID) long userId,
                              @PathVariable("item-id") long itemId) {
        return service.updateItem(userId, itemId, itemDto);
    }

    @GetMapping
    public Collection<ItemDto> getAllItemsByOwnerId(@RequestHeader(value = RequestHttpHeaders.USER_ID) long userId) {
        return service.getAllOwnerItems(userId);
    }

    @GetMapping("/search")
    public Collection<ItemDto> searchItems(@RequestParam String text) {
        return service.searchItems(text);
    }
}
