package ru.practicum.shareit.request.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.api.RequestHttpHeaders;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.SavedItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.Collection;

/**
 * TODO Sprint add-item-requests.
 */
@RestController
@RequestMapping(path = "/requests")
@AllArgsConstructor
public class ItemRequestController {

    private final ItemRequestService service;

    @PostMapping
    public ItemRequestDto createItemRequest(@RequestHeader(RequestHttpHeaders.USER_ID) Long userId,
                                            @RequestBody SavedItemRequestDto savedItemRequestDto) {
        return service.createItemRequest(userId, savedItemRequestDto);
    }

    @GetMapping
    public Collection<ItemRequestDto> getAllUserItemRequest(@RequestHeader(RequestHttpHeaders.USER_ID) Long userId) {
        return service.getAllUserItemRequests(userId);
    }

    @GetMapping("/all")
    public Collection<ItemRequestDto> getAllItemRequests(@RequestHeader(RequestHttpHeaders.USER_ID) Long userId) {
        return service.getAllItemRequests(userId);
    }

    @GetMapping("/{request-id}")
    public ItemRequestDto getItemRequest(@PathVariable(name = "request-id") Long requestId) {
        return service.getItemRequest(requestId);
    }
}
