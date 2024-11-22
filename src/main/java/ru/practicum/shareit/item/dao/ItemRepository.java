package ru.practicum.shareit.item.dao;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;

public interface ItemRepository {

    ItemDto addNewItem(ItemDto dto, long userId);

    ItemDto updateItem(ItemDto dto, long itemId, long userId);

    ItemDto getItemById(long itemId);

    Collection<ItemDto> getItemsByUserId(long userId);

    Collection<ItemDto> getItemsByCertainSearchPrompt(String text);
}
