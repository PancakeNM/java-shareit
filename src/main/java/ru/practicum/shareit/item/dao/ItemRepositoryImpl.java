package ru.practicum.shareit.item.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoMapper;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ItemRepositoryImpl implements ItemRepository {

    private static final Map<Long, Item> items = new HashMap<>();

    private final ItemDtoMapper mapper;

    private Long id = 0L;

    @Override
    public ItemDto addNewItem(ItemDto dto, Long userId) {
        Item newItem = mapper.mapItemFromDto(dto)
                .toBuilder()
                .ownerId(userId)
                .id(generateNewId())
                .build();
        items.put(newItem.getId(), newItem);
        dto.toBuilder().id(newItem.getId()).build();
        return dto;
    }

    @Override
    public ItemDto updateItem(ItemDto dto, Long itemId, Long userId) {
        log.info("User id={} is updating item id={}", userId, itemId);
        Item oldItem = findItem(itemId);
        if (oldItem.getOwnerId().equals(userId)) {
            log.trace("updating item id={}", itemId);
            updateItem(oldItem, dto);
            log.info("Item updated");
        } else {
            log.error("user id={} not allowed to modify item id={}", userId, itemId);
            throw new AccessDeniedException(String.format("Пользователь под id = %s не является " +
                    "владельцем предмета под id = %s", userId, itemId));
        }
        return mapper.mapDtoFromItem(items.get(itemId));
    }

    @Override
    public ItemDto getItemById(Long itemId) {
        log.info("getting item id={}", itemId);
        return mapper.mapDtoFromItem(findItem(itemId));
    }

    @Override
    public Collection<ItemDto> getItemsByUserId(Long userId) {
        log.info("getting all items with ownerId={}", userId);
        return items.values().stream()
                .filter(item -> item.getOwnerId().equals(userId))
                .map(mapper::mapDtoFromItem)
                .toList();
    }

    @Override
    public Collection<ItemDto> getItemsByCertainSearchPrompt(String text) {
        log.info("Searching for items by search prompt='{}'", text);
        return items.values().stream()
                .filter(item -> item.getName().contains(text) || item.getDescription().contains(text))
                .map(mapper::mapDtoFromItem)
                .toList();
    }

    private Long generateNewId() {
        return id++;
    }

    private void updateItem(Item oldItem, ItemDto dto) {
        if (!dto.getName().isBlank()) {
            log.trace("Updating name");
            oldItem.setName(dto.getName());
        }
        if (!dto.getDescription().isBlank()) {
            log.trace("Updating description");
            oldItem.setDescription(dto.getDescription());
        }
        if (!dto.getAvailable().equals(oldItem.getAvailable())) {
            log.trace("Updating availability status");
            oldItem.setAvailable(dto.getAvailable());
        }
    }

    private Item findItem(Long itemId) {
        try {
            return items.get(itemId);
        } catch (NullPointerException e) {
            log.error("Item id={} is not found", itemId);
            throw new NotFoundException(String.format("Предмет под id = %s не найден", itemId));
        }
    }
}
