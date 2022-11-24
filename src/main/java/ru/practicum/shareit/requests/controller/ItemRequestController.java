package ru.practicum.shareit.requests.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.controller.validator.RequestIdExist;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.service.ItemRequestService;
import ru.practicum.shareit.user.controller.validator.UserIdExist;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

/**
 * // The class handles the user's request in "/requests".
 */
@Validated
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {

    private final ItemRequestService requestService;

    @PostMapping
    public ItemRequestDto createRequest(@RequestHeader(value = "X-Sharer-User-Id") @UserIdExist Long userId,
                                        @Validated @RequestBody ItemRequestDto itemRequestDto) {
        return requestService.create(userId, itemRequestDto);
    }

    @GetMapping
    public List<ItemRequestDto> getRequests(@RequestHeader(value = "X-Sharer-User-Id") @UserIdExist Long userId) {
        return requestService.getByUser(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAllRequests(@RequestHeader(value = "X-Sharer-User-Id") @UserIdExist Long userId,
                                               @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero int from,
                                               @RequestParam(name = "size", defaultValue = "10") @Positive int size) {
        //PageRequest pageRequest = PageRequest.of(from/size, size, Sort.by("created").descending());
        PageRequest pageRequest = PageRequest.of(from / size, size);
        return requestService.getAll(userId, pageRequest);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getRequest(@RequestHeader(value = "X-Sharer-User-Id") @UserIdExist Long userId,
                                     @PathVariable(required = true) @NotNull @RequestIdExist Long requestId) {
        return requestService.get(requestId);
    }


}
