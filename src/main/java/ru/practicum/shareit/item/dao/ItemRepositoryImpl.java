package ru.practicum.shareit.item.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoMapper;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ItemRepositoryImpl implements ItemRepository {

    private final Map<Long, Item> items = new HashMap<>();
    private final ItemDtoMapper mapper;

    private Long id = 0L;

    @Override
    public ItemDto addNewItem(ItemDto dto, long userId) {
        Item newItem = mapper.mapItemFromDto(dto)
                .toBuilder()
                .ownerId(userId)
                .id(generateNewId())
                .build();
        items.put(newItem.getId(), newItem);
        return dto.toBuilder().id(newItem.getId()).build();
    }

    @Override
    public ItemDto updateItem(ItemDto dto, long itemId, long userId) {
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
    public ItemDto getItemById(long itemId) {
        log.info("getting item id={}", itemId);
        return mapper.mapDtoFromItem(findItem(itemId));
    }

    @Override
    public Collection<ItemDto> getItemsByUserId(long userId) {
        log.info("getting all items with ownerId={}", userId);
        return items.values().stream()
                .filter(item -> item.getOwnerId().equals(userId))
                .map(mapper::mapDtoFromItem)
                .toList();
    }

    @Override
    public Collection<ItemDto> getItemsByCertainSearchPrompt(String text) {
        log.info("Searching for items by search prompt");
        if (!text.isEmpty() && !text.isBlank()) {
            log.debug("Search prompt='{}'", text);
            return items.values().stream()
                    .filter(item -> (item.getName().toLowerCase().contains(text.toLowerCase())
                            || item.getDescription().toLowerCase().contains(text.toLowerCase())) &&
                            item.getAvailable())
                    .map(mapper::mapDtoFromItem)
                    .toList();
        } else {
            log.debug("Search prompt is empty, returning empty list");
            return new ArrayList<>();
        }
    }

    private Long generateNewId() {
        long oldId = id;
        id = id + 1;
        return oldId;
    }

    private void updateItem(Item oldItem, ItemDto dto) {
        if (dto.getName() != null) {
            log.trace("Updating name");
            oldItem.setName(dto.getName());
        }
        if (dto.getDescription() != null) {
            log.trace("Updating description");
            oldItem.setDescription(dto.getDescription());
        }
        if (dto.getAvailable() != null && !dto.getAvailable().equals(oldItem.getAvailable())) {
            log.trace("Updating availability status");
            oldItem.setAvailable(dto.getAvailable());
        }
    }

    private Item findItem(Long itemId) {
        Item item = items.get(itemId);
        if (item != null) {
            return item;
        } else {
            log.error("Item id={} is not found", itemId);
            throw new NotFoundException(String.format("Предмет под id = %s не найден", itemId));
        }
    }
}
