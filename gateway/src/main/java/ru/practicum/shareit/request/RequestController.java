package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;


@Validated
@Slf4j
@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
public class RequestController {
    private final RequestClient client;


    @PostMapping
    public ResponseEntity<Object> createRequest(@RequestHeader(value = "X-Sharer-User-Id") @Positive Long userId,
                                                @Validated @RequestBody RequestDto itemRequestDto) {
        log.info("Try to create request. User:{}, Item description:{}", userId, itemRequestDto.getDescription());
        return client.create(userId, itemRequestDto);
    }

    @GetMapping
    public ResponseEntity<Object> getRequests(@RequestHeader(value = "X-Sharer-User-Id") @Positive Long userId) {
        log.info("Try to get requests by user. User: {}", userId);
        return client.getByUser(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllRequests(@RequestHeader(value = "X-Sharer-User-Id") @Positive Long userId,
                                                 @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero int from,
                                                 @RequestParam(name = "size", defaultValue = "10") @Positive int size) {
        log.info("Try to get all requests. User:{}. An return a {} items from {}.", userId, size, from);
        return client.getAll(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequest(@RequestHeader(value = "X-Sharer-User-Id") @Positive Long userId,
                                             @PathVariable(required = true) @Positive Long requestId) {
        log.info("Try to get request. Request:{}", requestId);
        return client.get(requestId, userId);
    }

}
