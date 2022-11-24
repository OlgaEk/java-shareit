package ru.practicum.shareit.item.storage.memory;

import ru.practicum.shareit.item.dto.ItemCommentInfoDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStorage {
    Item create(Item item);

    void delete(Long id);

    Item getById(Long id);

    List<Item> getByUser(Long id);

    Item update(ItemCommentInfoDto itemCommentInfoDto);

    List<Item> search(String text);

    boolean containsItemById(Long id);
}
