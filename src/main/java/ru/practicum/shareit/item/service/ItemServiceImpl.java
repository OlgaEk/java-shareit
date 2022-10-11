package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.AccessNotAllowed;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemStorage itemStorage;
    private final ItemMapper itemMapper;

    public ItemDto create(Long userId, ItemDto itemDto) {
        Item item = itemMapper.dtoToItem(itemDto);
        item.setOwnerId(userId);
        return itemMapper.itemToDto(itemStorage.create(item));
    }

    public void delete(Long id) {
        itemStorage.delete(id);
    }

    public ItemDto update(Long userId, Long itemId, ItemDto itemDto) {
        Item itemToUpdate = itemStorage.getById(itemId);
        if (!Objects.equals(itemToUpdate.getOwnerId(), userId))
            throw new AccessNotAllowed("User id = " + userId + "is not owner");
        itemDto.setId(itemId);
        return itemMapper.itemToDto(itemStorage.update(itemDto));
    }

    public ItemDto getById(Long id) {
        return itemMapper.itemToDto(itemStorage.getById(id));
    }

    public List<ItemDto> getByUser(Long id) {
        return itemStorage.getByUser(id).stream().map(itemMapper::itemToDto).collect(Collectors.toList());
    }

    public List<ItemDto> search(String text) {
        if (text.isEmpty()) return new ArrayList<>();
        text = text.toLowerCase();
        return itemStorage.search(text).stream().map(itemMapper::itemToDto).collect(Collectors.toList());

    }


}
