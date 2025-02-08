package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.SavedItemRequestDto;

import java.util.Collection;

public interface ItemRequestService {

    ItemRequestDto createItemRequest(long userId, SavedItemRequestDto savedItemRequestDto);

    Collection<ItemRequestDto> getAllUserItemRequests(long userId);

    Collection<ItemRequestDto> getAllItemRequests(long userId);

    ItemRequestDto getItemRequest(long requestId);
}
