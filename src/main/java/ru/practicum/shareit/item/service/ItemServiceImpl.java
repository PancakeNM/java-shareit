package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserService userService;

    @Override
    public ItemDto save(ItemDto dto, long userId) {
        userService.getById(userId);
        return itemRepository.addNewItem(dto, userId);
    }

    @Override
    public ItemDto update(ItemDto dto, long itemId, long userId) {
        userService.getById(userId);
        return itemRepository.updateItem(dto, itemId, userId);
    }

    @Override
    public ItemDto getItemById(Long itemId) {
        return itemRepository.getItemById(itemId);
    }

    @Override
    public Collection<ItemDto> getItemsByUserId(long userId) {
        userService.getById(userId);
        return itemRepository.getItemsByUserId(userId);
    }

    @Override
    public Collection<ItemDto> getItemsByCertainSearchPrompt(String text) {
        return itemRepository.getItemsByCertainSearchPrompt(text);
    }
}
