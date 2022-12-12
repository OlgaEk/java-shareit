package ru.practicum.shareit.requests.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.controller.validator.RequestIdExist;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.service.ItemRequestService;
import ru.practicum.shareit.user.controller.validator.UserIdExist;

import java.util.List;

/**
 * // The class handles the user's request in "/requests".
 */
@Validated
@Slf4j
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {

    private final ItemRequestService requestService;

    @PostMapping
    public ItemRequestDto createRequest(@RequestHeader(value = "X-Sharer-User-Id") @UserIdExist Long userId,
                                        @Validated @RequestBody ItemRequestDto itemRequestDto) {
        log.info("Try to create request. User:{}, Item description:{}", userId, itemRequestDto.getDescription());
        return requestService.create(userId, itemRequestDto);
    }

    @GetMapping
    public List<ItemRequestDto> getRequests(@RequestHeader(value = "X-Sharer-User-Id") @UserIdExist Long userId) {
        log.info("Try to get requests by user. User: {}", userId);
        return requestService.getByUser(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAllRequests(@RequestHeader(value = "X-Sharer-User-Id") @UserIdExist Long userId,
                                               @RequestParam(name = "from", defaultValue = "0") int from,
                                               @RequestParam(name = "size", defaultValue = "10") int size) {
        //PageRequest pageRequest = PageRequest.of(from/size, size, Sort.by("created").descending());
        log.info("Try to get all requests. User:{}. An return a {} items from {}.", userId, size, from);
        PageRequest pageRequest = PageRequest.of(from / size, size);
        return requestService.getAll(userId, pageRequest);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getRequest(@RequestHeader(value = "X-Sharer-User-Id") @UserIdExist Long userId,
                                     @PathVariable(required = true) @RequestIdExist Long requestId) {
        log.info("Try to get request. Request:{}", requestId);
        return requestService.get(requestId);
    }


}
