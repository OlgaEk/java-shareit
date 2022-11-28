package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.requests.storage.ItemRequestRepository;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@JsonTest
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
class ItemMapperTest {

    @Autowired
    private JacksonTester<ItemRequestInfoDto> json;

    @Mock
    private ItemRequestRepository repository;

    @InjectMocks
    private ItemMapperImpl mapper;


    @Test
    void jsonTest() throws IOException {
        ItemRequestInfoDto itemRequest = new ItemRequestInfoDto();
        itemRequest.setId(1L);
        itemRequest.setName("testName");
        itemRequest.setDescription("test");
        itemRequest.setAvailable(true);
        itemRequest.setRequestId(1L);

        JsonContent<ItemRequestInfoDto> result = json.write(itemRequest);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("testName");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("test");
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(true);
        assertThat(result).extractingJsonPathNumberValue("$.requestId").isEqualTo(1);
    }

    @Test
    void shouldMapIdOnRequest() {
        ItemRequest request = new ItemRequest();
        request.setId(1L);

        assertNull(mapper.idToRequest(null));
        when(repository.findById(anyLong())).thenReturn(Optional.of(request));
        ItemRequest result = mapper.idToRequest(1L);
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }
}