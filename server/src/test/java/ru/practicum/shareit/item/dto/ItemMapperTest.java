package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.BeforeEach;
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
import ru.practicum.shareit.booking.dto.BookingBasicDataDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.NoEntityException;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.CommentRepository;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.requests.storage.ItemRequestRepository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@JsonTest
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
class ItemMapperTest {
    private Item item;
    private ItemCommentInfoDto itemCommentInfoDto;
    private ItemRequest itemRequest;
    @Autowired
    private JacksonTester<ItemRequestInfoDto> json;
    @Mock
    private ItemRequestRepository repository;
    @Mock
    private BookingService bookingService;
    @Mock
    private CommentMapper commentMapper;
    @Mock
    private CommentRepository commentRepository;
    @InjectMocks
    private ItemMapperImpl mapper;

    @BeforeEach
    private void setUp() {
        item = new Item();
        item.setId(1L);
        item.setName("Name");
        item.setAvailable(true);

        itemCommentInfoDto = new ItemCommentInfoDto();
        itemCommentInfoDto.setId(1L);
        itemCommentInfoDto.setName("Name");
        itemCommentInfoDto.setAvailable(true);

        itemRequest = new ItemRequest();
    }


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

        assertThrows(NoEntityException.class, () -> mapper.idToRequest(1L));

        assertNull(mapper.idToRequest(null));
        when(repository.findById(anyLong())).thenReturn(Optional.of(request));
        ItemRequest result = mapper.idToRequest(1L);
        assertNotNull(result);
        assertEquals(1L, result.getId());

        assertNull(mapper.idToRequest(null));

    }

    @Test
    void verifyItemToDto() {
        ItemCommentInfoDto result = mapper.itemToDto(item);
        assertNotNull(result);
        assertAll(
                () -> assertEquals(item.getId(), result.getId()),
                () -> assertEquals(item.getName(), result.getName()),
                () -> assertEquals(item.isAvailable(), result.getAvailable())
        );

        assertNull(mapper.itemToDto(null));

        item.setRequest(itemRequest);
        ItemCommentInfoDto resultNullRequest = mapper.itemToDto(item);
        assertNotNull(result);
        assertAll(
                () -> assertEquals(item.getId(), resultNullRequest.getId()),
                () -> assertEquals(item.getName(), resultNullRequest.getName()),
                () -> assertEquals(item.isAvailable(), resultNullRequest.getAvailable()),
                () -> assertEquals(item.getRequest().getId(), resultNullRequest.getRequestId())
        );
        itemRequest.setId(1L);
        ItemCommentInfoDto resultRequest = mapper.itemToDto(item);
        assertNotNull(result);
        assertAll(
                () -> assertEquals(item.getId(), resultRequest.getId()),
                () -> assertEquals(item.getName(), resultRequest.getName()),
                () -> assertEquals(item.isAvailable(), resultRequest.getAvailable()),
                () -> assertEquals(item.getRequest().getId(), resultRequest.getRequestId())
        );
    }

    @Test
    void verifyItemToBookInfoDto() {
        when(bookingService.getLastBooking(anyLong())).thenReturn(new BookingBasicDataDto());
        when(bookingService.getNextBooking(anyLong())).thenReturn(new BookingBasicDataDto());
        when(commentMapper.commentsToDto(any())).thenReturn(List.of(new CommentDto()));
        when(commentRepository.findAllByItemId(anyLong())).thenReturn(List.of(new Comment()));

        ItemBookInfoDto result = mapper.itemToBookInfoDto(item);
        assertNotNull(result);
        assertAll(
                () -> assertEquals(item.getId(), result.getId()),
                () -> assertEquals(item.getName(), result.getName()),
                () -> assertEquals(item.isAvailable(), result.getAvailable())
        );

        assertNull(mapper.itemToBookInfoDto(null));

    }

    @Test
    void verifyDtoToItem() {
        Item result = mapper.dtoToItem(itemCommentInfoDto);
        assertNotNull(result);
        assertAll(
                () -> assertEquals(itemCommentInfoDto.getId(), result.getId()),
                () -> assertEquals(itemCommentInfoDto.getName(), result.getName()),
                () -> assertEquals(itemCommentInfoDto.getAvailable(), result.isAvailable())
        );

        assertNull(mapper.dtoToItem(null));

    }

    @Test
    void verifyItemToRequestInfoDto() {
        ItemRequestInfoDto result = mapper.itemToRequestInfoDto(item);
        assertNotNull(result);
        assertAll(
                () -> assertEquals(item.getId(), result.getId()),
                () -> assertEquals(item.getName(), result.getName()),
                () -> assertEquals(item.isAvailable(), result.getAvailable())
        );

        assertNull(mapper.itemToRequestInfoDto(null));

    }


}