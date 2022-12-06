package ru.practicum.shareit.requests.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.requests.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto create(Long userId, ItemRequestDto requestDto);

    List<ItemRequestDto> getByUser(Long userId);

    List<ItemRequestDto> getAll(Long userId, Pageable pageable);

    ItemRequestDto get(Long requestId);
}
