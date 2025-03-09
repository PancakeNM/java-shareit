package ru.practicum.shareit.request.service;

import org.mapstruct.Mapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemResponseToRequestDto;
import ru.practicum.shareit.request.dto.SavedItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.Collection;

@Mapper(componentModel = "spring")
public interface ItemRequestMapper {

    ItemRequest map(SavedItemRequestDto savedItemRequestDto);

    ItemRequestDto map(ItemRequest itemRequest);

    Collection<ItemRequestDto> map(Collection<ItemRequest> itemRequests);

    Collection<ItemResponseToRequestDto> mapItems(Collection<Item> itemRequests);
}
