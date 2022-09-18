package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.controller.validator.ItemIdExist;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.validateGroup.Create;
import ru.practicum.shareit.user.controller.validator.UserIdExist;

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
    public ItemDto createItem(@RequestHeader(value = "X-Sharer-User-Id") @UserIdExist Long userId,
                              @Validated(Create.class) @RequestBody ItemDto itemDto) {
        return itemService.create(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader(value = "X-Sharer-User-Id") @UserIdExist Long userId,
                              @PathVariable @ItemIdExist Long itemId,
                              @Validated @RequestBody ItemDto itemDto) {
        return itemService.update(userId, itemId, itemDto);
    }

    @GetMapping
    public List<ItemDto> getUserItems(@RequestHeader(value = "X-Sharer-User-Id") @UserIdExist Long userId) {
        return itemService.getByUser(userId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItem(@PathVariable @ItemIdExist Long itemId) {
        return itemService.getById(itemId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItem(@RequestParam String text) {
        return itemService.search(text);
    }


}
