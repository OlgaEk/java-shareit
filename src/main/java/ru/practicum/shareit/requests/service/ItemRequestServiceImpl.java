package ru.practicum.shareit.requests.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NoEntityException;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.dto.RequestMapper;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.requests.storage.ItemRequestRepository;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository requestRepository;
    private final RequestMapper requestMapper;
    private final UserRepository userRepository;


    @Override
    public ItemRequestDto create(Long userId, ItemRequestDto requestDto) {
        ItemRequest request = requestMapper.dtoToRequest(requestDto);
        request.setRequester(userRepository.findById(userId)
                .orElseThrow((() -> new NoEntityException("User is not found"))));
        request.setCreated(LocalDateTime.now());
        return requestMapper.requestToDto(requestRepository.save(request));
    }

    @Override
    public List<ItemRequestDto> getByUser(Long userId) {
        return requestMapper.requestsToDto(requestRepository.findAllByRequesterIdOrderByIdAsc(userId));
    }

    public List<ItemRequestDto> getAll(Long userId, Pageable pageable) {
        return requestMapper
                .requestsToDto(requestRepository.findAllByRequesterIdNotOrderByCreatedDesc(userId, pageable));
    }

    @Override
    public ItemRequestDto get(Long requestId) {
        return requestMapper.requestToDto(requestRepository.findById(requestId)
                .orElseThrow(() -> new NoEntityException("Request with id = " + requestId + " is not found")));
    }
}
