package ru.practicum.shareit.requests.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.requests.model.ItemRequest;

import java.util.List;

@Mapper(componentModel = "spring", uses = ItemMapper.class)
public interface RequestMapper {
    ItemRequest dtoToRequest(ItemRequestDto itemRequestDto);

    @Mapping(source = "itemsOnRequest", target = "items", qualifiedByName = "itemToRequestInfoDto")
    ItemRequestDto requestToDto(ItemRequest itemRequest);


    List<ItemRequestDto> requestsToDto(List<ItemRequest> requests);
}
