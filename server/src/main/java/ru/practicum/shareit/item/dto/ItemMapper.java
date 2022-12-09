package ru.practicum.shareit.item.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.NoEntityException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.storage.CommentRepository;
import ru.practicum.shareit.requests.dto.RequestMapper;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.requests.storage.ItemRequestRepository;

@Mapper(componentModel = "spring", uses = {ItemService.class, RequestMapper.class})
//Проблем со временем работой маппера mapstruct при изменении с интерфейса на абстрактный класс не заметила.
//Я нашла этот механизм внедрения и использования методов бинов на сайте гида по Spring (https://www.baeldung.com/mapstruct)
//5.2. Inject Spring Components into the Mapper
//Посмотрела в сгенерированном коде обычный класс сгененрировался, толь с использование внедренных бинов
//Но про отражение сейчас не скажу. Начала читать, для меня это пока темный лес :((

public abstract class ItemMapper {
    @Autowired
    protected BookingService bookingService;
    @Autowired
    protected CommentRepository commentRepository;
    @Autowired
    protected CommentMapper commentMapper;
    @Autowired
    protected ItemRequestRepository requestRepository;


    @Mapping(source = "request.id", target = "requestId")
    public abstract ItemCommentInfoDto itemToDto(Item item);

    @Mapping(target = "lastBooking", expression = "java(bookingService.getLastBooking(item.getId()))")
    @Mapping(target = "nextBooking", expression = "java(bookingService.getNextBooking(item.getId()))")
    @Mapping(target = "comments",
            expression = "java(commentMapper.commentsToDto(commentRepository.findAllByItemId(item.getId())))")
    public abstract ItemBookInfoDto itemToBookInfoDto(Item item);


    @Mapping(source = "requestId", target = "request", qualifiedByName = "MapIdOnRequest")
    public abstract Item dtoToItem(ItemCommentInfoDto itemCommentInfoDto);

    @Named("itemToRequestInfoDto")
    @Mapping(source = "request.id", target = "requestId")
    public abstract ItemRequestInfoDto itemToRequestInfoDto(Item item);

    @Named("MapIdOnRequest")
    public ItemRequest idToRequest(Long requestId) {
        if (requestId == null) return null;
        return requestRepository.findById(requestId).orElseThrow(() -> new NoEntityException("Request is not found"));
    }


}

