package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
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

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

/**
 * // The class handles the item's request in "/items".
 */
@Validated
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemCommentInfoDto createItem(@RequestHeader(value = "X-Sharer-User-Id") @UserIdExist Long userId,
                                         @Validated(Create.class) @RequestBody ItemCommentInfoDto itemCommentInfoDto) {
        return itemService.create(userId, itemCommentInfoDto);
    }

    @PatchMapping("/{itemId}")
    public ItemCommentInfoDto updateItem(@RequestHeader(value = "X-Sharer-User-Id") @UserIdExist Long userId,
                                         @PathVariable @ItemIdExist Long itemId,
                                         @Validated(Update.class) @RequestBody ItemCommentInfoDto itemCommentInfoDto) {
        return itemService.update(userId, itemId, itemCommentInfoDto);
    }

    @GetMapping
    public List<ItemCommentInfoDto> getUserItems(@RequestHeader(value = "X-Sharer-User-Id") @UserIdExist Long userId,
                                                 @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero int from,
                                                 @RequestParam(name = "size", defaultValue = "10") @Positive int size) {
        PageRequest pageRequest = PageRequest.of(from / size, size);
        return itemService.getByUser(userId, pageRequest);
    }

    @GetMapping("/{itemId}")
    public ItemCommentInfoDto getItem(@RequestHeader(value = "X-Sharer-User-Id") @UserIdExist Long userId,
                                      @PathVariable @ItemIdExist Long itemId) {
        return itemService.getById(userId, itemId);
    }

    @GetMapping("/search")
    public List<ItemCommentInfoDto> searchItem(@RequestParam String text,
                                               @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero int from,
                                               @RequestParam(name = "size", defaultValue = "10") @Positive int size) {
        PageRequest pageRequest = PageRequest.of(from / size, size);
        return itemService.search(text, pageRequest);
    }


    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(@RequestHeader(value = "X-Sharer-User-Id") @UserIdExist Long userId,
                                    @PathVariable @ItemIdExist Long itemId,
                                    @Validated(Create.class) @RequestBody CommentDto commentDto) {
        return itemService.createComment(userId, itemId, commentDto);
    }


}
