package ru.practicum.shareit.item.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.CommentRepository;

@Mapper(componentModel = "spring")
public abstract class ItemMapper {
    @Autowired
    protected BookingService bookingService;
    @Autowired
    protected CommentRepository commentRepository;
    @Autowired
    protected CommentMapper commentMapper;


    public abstract  ItemDto itemToDto(Item item);

    @Mapping(target = "lastBooking", expression = "java(bookingService.getLastBooking(item.getId()))")
    @Mapping(target = "nextBooking", expression = "java(bookingService.getNextBooking(item.getId()))")
    @Mapping(target = "comments",
            expression = "java(commentMapper.commentsToDto(commentRepository.findAllByItemId(item.getId())))")
    public abstract  ItemInfoDto itemToInfoDto(Item item);


    public abstract  Item dtoToItem(ItemDto itemDto);


}

