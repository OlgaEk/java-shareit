package ru.practicum.shareit.item.dto;

import org.mapstruct.Mapper;
import ru.practicum.shareit.item.model.Item;

@Mapper
public interface ItemMapper {
    ItemDto itemToDto(Item item);

    Item dtoToItem(ItemDto itemDto);

}

