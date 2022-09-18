package ru.practicum.shareit.item.storage;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ItemStorageInMemory implements ItemStorage {
    private final HashMap<Long, Item> itemBase = new HashMap<>();
    private Long lastAssignedId = 0L;

    public Item create(Item item) {
        item.setId(++lastAssignedId);
        itemBase.put(item.getId(), item);
        return item;
    }

    public void delete(Long id) {
        itemBase.remove(id);
    }

    public Item getById(Long id) {
        return itemBase.get(id);
    }

    public List<Item> getByUser(Long id) {
        return itemBase.values().stream().filter(item -> item.getOwnerId().equals(id)).collect(Collectors.toList());
    }

    public Item update(ItemDto itemDto) {
        Item itemToUpdate = itemBase.get(itemDto.getId());
        if (itemDto.getName() != null) {
            if (!itemDto.getName().isBlank())
                itemToUpdate.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            if (!itemDto.getDescription().isBlank())
                itemToUpdate.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            itemToUpdate.setAvailable(itemDto.getAvailable());
        }
        itemBase.put(itemToUpdate.getId(), itemToUpdate);
        return itemToUpdate;
    }

    public boolean containsItemById(Long id) {
        return itemBase.containsKey(id);
    }

    public List<Item> search(String text) {
        return itemBase.values().stream()
                .filter(Item::isAvailable)
                .filter(item -> item.getName().toLowerCase().contains(text)
                        || item.getDescription().toLowerCase().contains(text))
                .collect(Collectors.toList());
    }

}
