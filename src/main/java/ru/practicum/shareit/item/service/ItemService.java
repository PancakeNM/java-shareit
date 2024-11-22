package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;

public interface ItemService {

    ItemDto save(ItemDto dto, long userId);

    ItemDto update(ItemDto dto, long itemId, long userId);

    ItemDto getItemById(Long itemId);

    Collection<ItemDto> getItemsByUserId(long userId);

    Collection<ItemDto> getItemsByCertainSearchPrompt(String text);
}
