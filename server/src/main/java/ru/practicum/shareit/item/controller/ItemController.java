package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.controller.validator.ItemIdExist;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemCommentInfoDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.validateGroup.Create;
import ru.practicum.shareit.item.validateGroup.Update;
import ru.practicum.shareit.user.controller.validator.UserIdExist;

import java.util.List;

/**
 * // The class handles the item's request in "/items".
 */
@Validated
@Slf4j
@RestController

@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemCommentInfoDto createItem(@RequestHeader(value = "X-Sharer-User-Id") @UserIdExist Long userId,
                                         @Validated(Create.class) @RequestBody ItemCommentInfoDto itemCommentInfoDto) {
        log.info("Try to create a new item. User: {}, Item name : {}.", userId, itemCommentInfoDto.getName());
        return itemService.create(userId, itemCommentInfoDto);
    }

    @PatchMapping("/{itemId}")
    public ItemCommentInfoDto updateItem(@RequestHeader(value = "X-Sharer-User-Id") @UserIdExist Long userId,
                                         @PathVariable @ItemIdExist Long itemId,
                                         @Validated(Update.class) @RequestBody ItemCommentInfoDto itemCommentInfoDto) {
        log.info("Try to update item. User: {}, Item : {}", userId, itemId);
        return itemService.update(userId, itemId, itemCommentInfoDto);
    }

    @GetMapping
    public List<ItemCommentInfoDto> getUserItems(@RequestHeader(value = "X-Sharer-User-Id") @UserIdExist Long userId,
                                                 @RequestParam(name = "from", defaultValue = "0") int from,
                                                 @RequestParam(name = "size", defaultValue = "10") int size) {
        log.info("Try to get items by owner. Owner: {}. An return a {} items from {}.", userId, size, from);
        PageRequest pageRequest = PageRequest.of(from / size, size);
        return itemService.getByUser(userId, pageRequest);
    }

    @GetMapping("/{itemId}")
    public ItemCommentInfoDto getItem(@RequestHeader(value = "X-Sharer-User-Id") @UserIdExist Long userId,
                                      @PathVariable @ItemIdExist Long itemId) {
        return itemService.getById(userId, itemId);
    }

    //Возникла проблема при возвращении Page. Я для маппинга DTO классов использую mapstruct.
    // Оказалась он не умеет преобразовывать Page в Page....
    // И я пока никак не смогла разобраться как имплементировать в mapstruct собственные методы....
    // Единственно у меня есть только одна идея: это создать свой класс MyPage в него загрузить данные при маппинге
    // и потом уже в контролере преобразовывать MyPage в Page... но мне что-то не очень эта идея нравится.
    // Я еще попытаюсь у наставника поспрашивать, как можно преобразовать.
    @GetMapping("/search")
    public List<ItemCommentInfoDto> searchItem(@RequestParam String text,
                                               @RequestParam(name = "from", defaultValue = "0") int from,
                                               @RequestParam(name = "size", defaultValue = "10") int size) {
        log.info("Try to search items by text : {} in name or description. An return a {} items from {}.",
                text, size, from);
        PageRequest pageRequest = PageRequest.of(from / size, size);
        return itemService.search(text, pageRequest);
    }


    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(@RequestHeader(value = "X-Sharer-User-Id") @UserIdExist Long userId,
                                    @PathVariable @ItemIdExist Long itemId,
                                    @Validated(Create.class) @RequestBody CommentDto commentDto) {
        log.info("Try to create comment to item. User:{}, Item:{}, Comment: {}", userId, itemId, commentDto.getText());
        return itemService.createComment(userId, itemId, commentDto);
    }


}
