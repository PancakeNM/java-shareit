package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;

public interface ItemService {

    ItemDto save(ItemDto dto, Long userId);

    ItemDto update(ItemDto dto, Long itemId, Long userId);

    ItemDto getItemById(Long itemId);

    Collection<ItemDto> getItemsByUserId(Long userId);

    Collection<ItemDto> getItemsByCertainSearchPrompt(String text);
}
