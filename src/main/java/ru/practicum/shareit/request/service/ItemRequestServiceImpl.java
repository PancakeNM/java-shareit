package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemMapper;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemResponseToRequestDto;
import ru.practicum.shareit.request.dto.SavedItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final UserRepository userRepo;
    private final ItemRepository itemRepo;
    private final ItemMapper itemMapper;
    private final ItemRequestRepository itemRequestRepo;
    private final ItemRequestMapper itemRequestMapper;

    @Override
    public ItemRequestDto createItemRequest(long userId, SavedItemRequestDto savedItemRequestDto) {
        User user = userRepo.findById(userId).orElseThrow(
                () -> new NotFoundException("Пользователь с id " + userId + " не найден."));
        ItemRequest itemRequest = itemRequestMapper.map(savedItemRequestDto);
        itemRequest.setRequester(user);
        ItemRequest savedItemRequest = itemRequestRepo.save(itemRequest);
        return itemRequestMapper.map(savedItemRequest);
    }

    @Override
    public Collection<ItemRequestDto> getAllUserItemRequests(long userId) {
        Collection<ItemRequestDto> result = itemRequestMapper.map(
                itemRequestRepo.findAllByRequesterIdOrderByCreatedDesc(userId));
        return getRequestsWithItems(userId, result);
    }

    @Override
    public Collection<ItemRequestDto> getAllItemRequests(long userId) {
        Collection<ItemRequestDto> result = itemRequestMapper
                .map(itemRequestRepo.findAllByRequester_IdNotInOrderByCreatedDesc(List.of(userId)));
        return getRequestsWithItems(userId, result);
    }

    @Override
    public ItemRequestDto getItemRequest(long requestId) {
        ItemRequest itemRequest = itemRequestRepo.findById(requestId)
                .orElseThrow(
                        () -> new NotFoundException("Заявка с id " + requestId + " не найдена."));
        Collection<Item> items = itemRepo.findAllByRequestId(requestId);
        ItemRequestDto itemRequestDto = itemRequestMapper.map(itemRequest);
        itemRequestDto.setItems(itemMapper.mapToResponseToRequest(items));
        return itemRequestDto;
    }

    private Collection<ItemRequestDto> getRequestsWithItems(long userId, Collection<ItemRequestDto> result) {
        Collection<Long> itemIds = itemRequestRepo.findAllByRequesterIdOrderByCreatedDesc(userId)
                .stream()
                .map(ItemRequest::getId)
                .toList();
        Collection<ItemResponseToRequestDto> items = itemRequestMapper.mapItems(itemRepo.findAllByIdIn(itemIds));
        Map<Long, List<ItemResponseToRequestDto>> itemMap = items.stream()
                .collect(Collectors.groupingBy(ItemResponseToRequestDto::getId));
        return result.stream()
                .peek(itemRequest -> itemRequest.setItems(itemMap.get(itemRequest.getId())))
                .collect(Collectors.toList());
    }
}
