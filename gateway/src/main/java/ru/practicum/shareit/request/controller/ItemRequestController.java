package ru.practicum.shareit.request.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.api.RequestHttpHeaders;
import ru.practicum.shareit.request.client.ItemRequestClient;
import ru.practicum.shareit.request.dto.SavedItemRequestDto;

/**
 * TODO Sprint add-item-requests.
 */
@RestController
@RequestMapping(path = "/requests")
@AllArgsConstructor
@Validated
public class ItemRequestController {

    private final ItemRequestClient client;

    @PostMapping
    public ResponseEntity<Object> createItemRequest(@RequestHeader(RequestHttpHeaders.USER_ID) Long userId,
                                                    @RequestBody SavedItemRequestDto savedItemRequestDto) {
        return client.createItemRequest(userId, savedItemRequestDto);
    }

    @GetMapping
    public ResponseEntity<Object> getAllUserItemRequest(@RequestHeader(RequestHttpHeaders.USER_ID) Long userId) {
        return client.getAllUserItemRequest(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllItemRequests(@RequestHeader(RequestHttpHeaders.USER_ID) Long userId) {
        return client.getAllItemRequests(userId);
    }

    @GetMapping("/{request-id}")
    public ResponseEntity<Object> getItemRequest(@PathVariable(name = "request-id") Long requestId) {
        return client.getItemRequest(requestId);
    }
}
