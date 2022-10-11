package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto create(Long userId, ItemDto itemDto);

    void delete(Long id);

    ItemDto update(Long userId, Long itemId, ItemDto itemDto);

    ItemDto getById(Long id);

    List<ItemDto> getByUser(Long id);

    List<ItemDto> search(String text);

}
