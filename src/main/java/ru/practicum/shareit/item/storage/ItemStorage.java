package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStorage {
    Item create(Item item);

    void delete(Long id);

    Item getById(Long id);

    List<Item> getByUser(Long id);

    Item update(ItemDto itemDto);

    List<Item> search(String text);

    boolean containsItemById(Long id);
}
