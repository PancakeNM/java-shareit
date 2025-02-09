package ru.practicum.shareit.item.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.api.RequestHttpHeaders;
import ru.practicum.shareit.api.ValidateCreateRequest;
import ru.practicum.shareit.item.client.ItemClient;
import ru.practicum.shareit.item.dto.SavedCommentDto;
import ru.practicum.shareit.item.dto.SavedItemDto;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/items")
@Validated
public class ItemController {

    private final ItemClient client;

    @GetMapping("/{item-id}")
    public ResponseEntity<Object> getItem(@PathVariable("item-id") long itemId) {
        return client.getItem(itemId);
    }

    @PostMapping
    public ResponseEntity<Object> addItem(@RequestBody @Validated(ValidateCreateRequest.class) SavedItemDto itemDto,
                                          @RequestHeader(value = RequestHttpHeaders.USER_ID) long userId) {
        return client.addItem(userId, itemDto);
    }

    @PostMapping("/{item-id}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader(value = RequestHttpHeaders.USER_ID) long userId,
                                             @PathVariable("item-id") long itemId,
                                             @RequestBody @Validated(ValidateCreateRequest.class)
                                                 SavedCommentDto savedCommentDto) {
        return client.addComment(userId, itemId, savedCommentDto);
    }

    @PatchMapping("/{item-id}")
    public ResponseEntity<Object> updateItem(@RequestBody SavedItemDto itemDto,
                                             @RequestHeader(value = RequestHttpHeaders.USER_ID) long userId,
                                             @PathVariable("item-id") long itemId) {
        return client.updateItem(userId, itemId, itemDto);
    }

    @GetMapping
    public ResponseEntity<Object> getAllItemsByOwnerId(@RequestHeader(value = RequestHttpHeaders.USER_ID) long userId) {
        return client.getAllOwnerItems(userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItems(@RequestParam String text) {
        return text.isBlank() ? ResponseEntity.ok(List.of()) : client.searchItems(text);
    }
}
