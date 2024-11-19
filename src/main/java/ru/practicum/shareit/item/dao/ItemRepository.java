package ru.practicum.shareit.item.dao;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;

public interface ItemRepository {

    ItemDto addNewItem(ItemDto dto, Long userId);

    ItemDto updateItem(ItemDto dto, Long itemId, Long userId);

    ItemDto getItemById(Long itemId);

    Collection<ItemDto> getItemsByUserId(Long userId);

    Collection<ItemDto> getItemsByCertainSearchPrompt(String text);
}
