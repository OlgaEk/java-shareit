package ru.practicum.shareit.requests.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.exception.NoEntityException;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.dto.RequestMapper;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.requests.storage.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
class ItemRequestServiceImplTest {
    @Mock
    private ItemRequestRepository requestRepository;
    @Mock
    private RequestMapper mapper;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ItemRequestServiceImpl requestService;

    @Test
    void shouldCreateRequest() {
        ItemRequest requestReturn = new ItemRequest();
        requestReturn.setId(1L);
        ItemRequestDto requestDtoReturn = new ItemRequestDto();
        requestDtoReturn.setId(1L);

        when(mapper.dtoToRequest(any())).thenReturn(requestReturn);
        when(userRepository.findById(1L)).thenReturn(Optional.of(new User()));
        when(requestRepository.save(any())).thenReturn(requestReturn);
        when(mapper.requestToDto(any())).thenReturn(requestDtoReturn);

        ItemRequestDto requestDto = requestService.create(1L, new ItemRequestDto());
        assertNotNull(requestDto);
        assertEquals(1L, requestDto.getId());
        assertThrows(NoEntityException.class, () -> requestService.create(2L, new ItemRequestDto()));

        verify(mapper).requestToDto(any());
        verify(requestRepository).save(any());
        verify(userRepository).findById(1L);
        verify(mapper, times(2)).dtoToRequest(any());
        verify(userRepository).findById(2L);

        verifyNoMoreInteractions(requestRepository);
        verifyNoMoreInteractions(mapper);
        verifyNoMoreInteractions(userRepository);

    }


    @Test
    void shouldGetAllRequestAndAllRequestByUser() {
        PageRequest pageable = PageRequest.of(1, 1);
        when(requestRepository.findAllByRequesterIdNotOrderByCreatedDesc(1L, pageable))
                .thenReturn(List.of(new ItemRequest(), new ItemRequest()));
        when(requestRepository.findAllByRequesterIdOrderByIdAsc(1L))
                .thenReturn(List.of(new ItemRequest(), new ItemRequest()));
        requestService.getAll(1L, pageable);
        requestService.getByUser(1L);
        verify(requestRepository).findAllByRequesterIdNotOrderByCreatedDesc(1L, pageable);
        verify(requestRepository).findAllByRequesterIdOrderByIdAsc(1L);
        verifyNoMoreInteractions(requestRepository);
    }

    @Test
    void shouldGetOneRequestById() {
        when(requestRepository.findById(1L)).thenReturn(Optional.of(new ItemRequest()));
        requestService.get(1L);
        verify(requestRepository).findById(1L);
        assertThrows(NoEntityException.class, () -> requestService.get(2L));
        verify(requestRepository).findById(2L);
        verifyNoMoreInteractions(requestRepository);
    }
}