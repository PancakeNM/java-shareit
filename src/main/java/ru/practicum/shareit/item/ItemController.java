package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.Collection;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemService service;

    @GetMapping("/{item-id}")
    public ItemDto getItemById(@PathVariable("item-id") Long itemId) {
        return service.getItemById(itemId);
    }

    @PostMapping
    public ItemDto saveNewItem(@RequestBody @Valid ItemDto dto,
                               @RequestHeader("X-Sharer-User-Id") Long userId) {
        return service.save(dto, userId);
    }

    @PatchMapping("/{item-id}")
    public ItemDto updateItem(@RequestBody ItemDto dto,
                              @RequestHeader("X-Sharer-User-Id") Long userId,
                              @PathVariable("item-id") Long itemId) {
        return service.update(dto, itemId, userId);
    }

    @GetMapping
    public Collection<ItemDto> getAllItemsByOwnerId(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return service.getItemsByUserId(userId);
    }

    @GetMapping("/search?text={}")
    public Collection<ItemDto> getItemsBySearchPrompt(@RequestParam String text) {
        return service.getItemsByCertainSearchPrompt(text);
    }
}
