package ru.practicum.shareit.item.service;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.SavedItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    Item map(SavedItemDto savedItemDto);

    ItemDto map(Item item);

    Collection<ItemDto> map(Collection<Item> items);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateItemFromDto(@MappingTarget Item item, SavedItemDto savedItemDto);
}
