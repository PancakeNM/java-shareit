package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    @Override
    public ItemDto save(ItemDto dto, Long userId) {
        return itemRepository.addNewItem(dto, userId);
    }

    @Override
    public ItemDto update(ItemDto dto, Long itemId, Long userId) {
        return itemRepository.updateItem(dto, itemId, userId);
    }

    @Override
    public ItemDto getItemById(Long itemId) {
        return itemRepository.getItemById(itemId);
    }

    @Override
    public Collection<ItemDto> getItemsByUserId(Long userId) {
        return itemRepository.getItemsByUserId(userId);
    }

    @Override
    public Collection<ItemDto> getItemsByCertainSearchPrompt(String text) {
        return itemRepository.getItemsByCertainSearchPrompt(text);
    }
}
