package ru.practicum.shareit.item.storage.memory;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemCommentInfoDto;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ItemStorageInMemory implements ItemStorage {
    private final Map<Long, Item> itemBase = new HashMap<>();
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
        return itemBase.values().stream()
                .filter(item -> item.getOwner().getId().equals(id))
                .collect(Collectors.toList());
    }

    public Item update(ItemCommentInfoDto itemCommentInfoDto) {
        Item itemToUpdate = itemBase.get(itemCommentInfoDto.getId());
        if (itemCommentInfoDto.getName() != null) {
            if (!itemCommentInfoDto.getName().isBlank())
                itemToUpdate.setName(itemCommentInfoDto.getName());
        }
        if (itemCommentInfoDto.getDescription() != null) {
            if (!itemCommentInfoDto.getDescription().isBlank())
                itemToUpdate.setDescription(itemCommentInfoDto.getDescription());
        }
        if (itemCommentInfoDto.getAvailable() != null) {
            itemToUpdate.setAvailable(itemCommentInfoDto.getAvailable());
        }
        itemBase.put(itemToUpdate.getId(), itemToUpdate);
        return itemToUpdate;
    }

    public boolean containsItemById(Long id) {
        return itemBase.containsKey(id);
    }

    public List<Item> search(String text) {
        return itemBase.values().stream()
                //.filter(Item::getAvailable)
                .filter(item -> item.getName().toLowerCase().contains(text)
                        || item.getDescription().toLowerCase().contains(text))
                .collect(Collectors.toList());
    }

}
