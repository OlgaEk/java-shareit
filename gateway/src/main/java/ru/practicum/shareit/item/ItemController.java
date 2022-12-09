package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemInputInfoDto;
import ru.practicum.shareit.validateGroup.Create;
import ru.practicum.shareit.validateGroup.Update;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;


@Validated
@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemClient client;

    @PostMapping
    public ResponseEntity<Object> createItem(@RequestHeader(value = "X-Sharer-User-Id") @Positive Long userId,
                                             @Validated(Create.class) @RequestBody ItemInputInfoDto itemDto) {
        log.info("Try to create a new item. User: {}, Item name : {}.", userId, itemDto.getName());
        return client.create(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestHeader(value = "X-Sharer-User-Id") @Positive Long userId,
                                             @PathVariable @Positive Long itemId,
                                             @Validated(Update.class) @RequestBody ItemInputInfoDto itemDto) {
        log.info("Try to update item. User: {}, Item : {}", userId, itemId);
        return client.update(userId, itemId, itemDto);
    }

    @GetMapping
    public ResponseEntity<Object> getUserItems(@RequestHeader(value = "X-Sharer-User-Id") @Positive Long userId,
                                               @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero int from,
                                               @RequestParam(name = "size", defaultValue = "10") @Positive int size) {
        log.info("Try to get items by owner. Owner: {}. An return a {} items from {}.", userId, size, from);
        return client.getByUser(userId, from, size);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItem(@RequestHeader(value = "X-Sharer-User-Id") @Positive Long userId,
                                          @PathVariable @Positive Long itemId) {
        return client.getById(userId, itemId);
    }


    @GetMapping("/search")
    public ResponseEntity<Object> searchItem(@RequestHeader(value = "X-Sharer-User-Id") @Positive Long userId,
                                             @RequestParam String text,
                                             @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero int from,
                                             @RequestParam(name = "size", defaultValue = "10") @Positive int size) {
        log.info("Try to search items by text : {} in name or description. An return a {} items from {}.",
                text, size, from);
        return client.search(text, userId, from, size);
    }


    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@RequestHeader(value = "X-Sharer-User-Id") @Positive Long userId,
                                                @PathVariable @Positive Long itemId,
                                                @Validated(Create.class) @RequestBody CommentDto commentDto) {
        log.info("Try to create comment to item. User:{}, Item:{}, Comment: {}", userId, itemId, commentDto.getText());
        return client.createComment(userId, itemId, commentDto);
    }

}
