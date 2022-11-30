package ru.practicum.shareit.requests.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.ItemRequestInfoDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.requests.model.ItemRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
public class RequestMapperTest {

    @InjectMocks
    private RequestMapperImpl mapper;
    private ItemRequestDto itemRequestDto;
    private ItemRequestInfoDto itemRequestInfoDto;
    private ItemRequest itemRequest;
    private Item item;
    @Mock
    private ItemMapper itemMapper;

    @BeforeEach
    void setUp() {
        itemRequestDto = new ItemRequestDto();
        itemRequestDto.setId(1L);

        itemRequest = new ItemRequest();
        itemRequest.setId(1L);

        item = new Item();
        item.setId(1L);

        itemRequestInfoDto = new ItemRequestInfoDto();
        itemRequestInfoDto.setId(1L);

    }

    @Test
    void verifyDtoToRequest() {
        ItemRequest result = mapper.dtoToRequest(itemRequestDto);
        assertNotNull(result);
        assertAll(
                () -> assertEquals(itemRequestDto.getId(), result.getId())
        );

        assertNull(mapper.dtoToRequest(null));
    }

    @Test
    void verifyRequestToDto() {
        ItemRequestDto result = mapper.requestToDto(itemRequest);
        assertNotNull(result);
        assertAll(
                () -> assertEquals(itemRequest.getId(), result.getId())
        );

        itemRequest.setItemsOnRequest(List.of(item));
        when(itemMapper.itemToRequestInfoDto(any())).thenReturn(itemRequestInfoDto);
        ItemRequestDto resultWithItems = mapper.requestToDto(itemRequest);
        assertNotNull(result);
        assertAll(
                () -> assertEquals(itemRequest.getId(), resultWithItems.getId()),
                () -> assertEquals(itemRequest.getItemsOnRequest().get(0).getId(),
                        resultWithItems.getItems().get(0).getId())
        );

        assertNull(mapper.requestToDto(null));
    }

    @Test
    void verifyRequestsToDto() {
        List<ItemRequestDto> result = mapper.requestsToDto(List.of(itemRequest));
        assertNotNull(result);
        assertAll(
                () -> assertEquals(1, result.size()),
                () -> assertEquals(itemRequest.getId(), result.get(0).getId())

        );

        assertNull(mapper.requestsToDto(null));

    }
}
