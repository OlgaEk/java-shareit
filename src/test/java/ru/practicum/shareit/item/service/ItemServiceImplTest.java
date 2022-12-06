package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.AccessNotAllowed;
import ru.practicum.shareit.exception.NoEntityException;
import ru.practicum.shareit.exception.NotValidRequestException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.CommentRepository;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
class ItemServiceImplTest {

    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private CommentMapper commentMapper;
    @Mock
    private CommentRepository commentRepository;

    @Mock
    private ItemMapper itemMapper;

    @InjectMocks
    private ItemServiceImpl itemService;

    private ItemCommentInfoDto itemInputDto;
    private ItemBookInfoDto itemBookDto;
    private Item item;
    private User user;

    private CommentDto comment;

    private PageRequest pageable;


    @BeforeEach
    void setUp() {
        itemInputDto = new ItemCommentInfoDto();
        itemInputDto.setId(1L);
        itemInputDto.setName("name");
        itemInputDto.setDescription("text");
        itemInputDto.setAvailable(true);

        itemBookDto = new ItemBookInfoDto();
        itemBookDto.setId(1L);

        comment = new CommentDto();
        comment.setId(1L);
        comment.setText("text");
        comment.setAuthorName("Name");
        comment.setCreated(LocalDateTime.now());

        user = new User();
        user.setId(1L);


        item = new Item();
        item.setOwner(user);

        pageable = PageRequest.of(0, 10);
    }

    @Test
    void shouldCreateItem() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        when(itemRepository.save(any(Item.class))).thenReturn(new Item());
        when(itemMapper.dtoToItem(any())).thenReturn(new Item());
        when(itemMapper.itemToDto(any())).thenReturn(itemInputDto);

        ItemCommentInfoDto result = itemService.create(1L, itemInputDto);
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(itemInputDto.getName(), result.getName());

        verify(userRepository).findById(anyLong());
        verify(itemRepository).save(any(Item.class));
        verify(itemMapper).dtoToItem(any());
        verify(itemMapper).itemToDto(any());

        verifyNoMoreInteractions(userRepository);
        verifyNoMoreInteractions(itemRepository);
        verifyNoMoreInteractions(itemMapper);

    }

    @Test
    void shouldThrowExceptionIfUserNotFound() {
        when(itemMapper.dtoToItem(any())).thenReturn(new Item());

        NoEntityException ex = assertThrows(NoEntityException.class,
                () -> itemService.create(1L, itemInputDto));
        assertEquals("User is not found", ex.getMessage());

        verify(itemMapper).dtoToItem(any());

        verifyNoMoreInteractions(itemMapper);
    }

    @Test
    void shouldDeleteItem() {
        itemService.delete(1L);
    }

    @Test
    void shouldUpdateItem() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.ofNullable(item));
        when(itemMapper.itemToDto(any())).thenReturn(itemInputDto);
        when(itemRepository.saveAndFlush(any())).thenReturn(new Item());

        ItemCommentInfoDto result = itemService.update(1L, 1L, itemInputDto);
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(itemInputDto.getDescription(), result.getDescription());

        verify(itemRepository).findById(anyLong());
        verify(itemMapper).itemToDto(any());
        verify(itemRepository).saveAndFlush(any());

        verifyNoMoreInteractions(itemRepository);
        verifyNoMoreInteractions(itemMapper);
    }

    @Test
    void shouldThrowExceptionIfItemNotFoundOrUserNotOwnerOfItem() {
        NoEntityException ex = assertThrows(NoEntityException.class,
                () -> itemService.update(1L, 1L, itemInputDto));
        assertEquals("Item is not found", ex.getMessage());
        when(itemRepository.findById(anyLong())).thenReturn(Optional.ofNullable(item));
        user.setId(2L);
        AccessNotAllowed exAccess = assertThrows(AccessNotAllowed.class,
                () -> itemService.update(1L, 1L, itemInputDto));
        assertEquals("User id = 1 is not owner", exAccess.getMessage());

        verify(itemRepository, times(2)).findById(anyLong());

        verifyNoMoreInteractions(itemRepository);
    }

    @Test
    void shouldGetItemById() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.ofNullable(item));
        when(itemMapper.itemToBookInfoDto(any())).thenReturn(itemBookDto);
        ItemCommentInfoDto result = itemService.getById(1L, 1L);
        assertNotNull(result);
        assertEquals(1L, result.getId());

        ItemBookInfoDto resultWithBook = (ItemBookInfoDto) itemService.getById(2L, 1L);
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertNull(resultWithBook.getLastBooking());
        assertNull(resultWithBook.getNextBooking());

        verify(itemRepository, times(2)).findById(anyLong());
        verify(itemMapper, times(2)).itemToBookInfoDto(any());

        verifyNoMoreInteractions(itemRepository);
        verifyNoMoreInteractions(itemMapper);
    }

    @Test
    void shouldThrowExceptionWhenItemNotFound() {
        NoEntityException ex = assertThrows(NoEntityException.class,
                () -> itemService.getById(1L, 1L));
        assertEquals("Item is not found", ex.getMessage());
    }

    @Test
    void shouldGetItemsByUser() {
        when(itemRepository.findByOwnerIdOrderByIdAsc(1L, pageable))
                .thenReturn(new PageImpl<>(List.of(new Item())));
        when(itemMapper.itemToBookInfoDto(any())).thenReturn(itemBookDto);
        List<ItemCommentInfoDto> result = itemService.getByUser(1L, pageable);
        assertNotNull(result);
        assertEquals(1L, result.get(0).getId());

        verify(itemRepository).findByOwnerIdOrderByIdAsc(1L, pageable);
        verify(itemMapper).itemToBookInfoDto(any());

        verifyNoMoreInteractions(itemRepository);
        verifyNoMoreInteractions(itemMapper);
    }

    @Test
    void shouldSearchItems() {
        when(itemRepository.findByNameAndDescription("text", pageable))
                .thenReturn(new PageImpl<>(List.of(new Item())));
        when(itemMapper.itemToDto(any())).thenReturn(itemInputDto);
        List<ItemCommentInfoDto> result = itemService.search("", pageable);
        assertEquals(0, result.size());
        result = itemService.search("text", pageable);
        assertNotNull(result);
        assertEquals(1L, result.get(0).getId());

        verify(itemRepository).findByNameAndDescription("text", pageable);
        verify(itemMapper).itemToDto(any());

        verifyNoMoreInteractions(itemRepository);
        verifyNoMoreInteractions(itemMapper);
    }

    @Test
    void shouldCreateComment() {
        when(bookingRepository.findAllByBookerIdAndItemId(anyLong(), anyLong(), any(LocalDateTime.class)))
                .thenReturn(List.of(new Booking()));
        when(commentMapper.dtoToComment(any())).thenReturn(new Comment());
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.ofNullable(item));
        when(commentRepository.save(any())).thenReturn(new Comment());
        when(commentMapper.commentToDto(any())).thenReturn(comment);

        CommentDto result = itemService.createComment(1L, 1L, comment);
        assertNotNull(result);
        assertAll(
                () -> assertEquals(1L, result.getId()),
                () -> assertEquals(comment.getText(), result.getText()),
                () -> assertEquals(comment.getAuthorName(), result.getAuthorName()),
                () -> assertNotNull(result.getCreated())
        );

        verify(bookingRepository).findAllByBookerIdAndItemId(anyLong(), anyLong(), any(LocalDateTime.class));
        verify(commentMapper).dtoToComment(any());
        verify(userRepository).findById(anyLong());
        verify(itemRepository).findById(anyLong());
        verify(commentRepository).save(any());
        verify(commentMapper).commentToDto(any());

        verifyNoMoreInteractions(bookingRepository);
        verifyNoMoreInteractions(commentMapper);
        verifyNoMoreInteractions(userRepository);
        verifyNoMoreInteractions(itemRepository);
        verifyNoMoreInteractions(commentRepository);
        verifyNoMoreInteractions(commentMapper);
    }

    @Test
    void shouldThrowExceptionWhenSaveCommentIfBookingUserOrItemNotFound() {
        NotValidRequestException exValid = assertThrows(NotValidRequestException.class,
                () -> itemService.createComment(1L, 1L, comment));
        assertEquals("User id = 1 didn't get the item id = 1", exValid.getMessage());

        when(bookingRepository.findAllByBookerIdAndItemId(anyLong(), anyLong(), any(LocalDateTime.class)))
                .thenReturn(List.of(new Booking()));
        NoEntityException ex = assertThrows(NoEntityException.class,
                () -> itemService.createComment(1L, 1L, comment));
        assertEquals("User is not found", ex.getMessage());

        when(commentMapper.dtoToComment(any())).thenReturn(new Comment());
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        ex = assertThrows(NoEntityException.class, () -> itemService.createComment(1L, 1L, comment));
        assertEquals("Item is not found", ex.getMessage());

        verify(bookingRepository, times(3))
                .findAllByBookerIdAndItemId(anyLong(), anyLong(), any(LocalDateTime.class));
        verify(commentMapper, times(2)).dtoToComment(any());
        verify(userRepository, times(2)).findById(anyLong());

        verifyNoMoreInteractions(bookingRepository);
        verifyNoMoreInteractions(commentMapper);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void shouldGetCommentsByItem() {
        when(commentMapper.commentsToDto(any())).thenReturn(List.of(comment));
        when(commentRepository.findAllByItemId(anyLong())).thenReturn(List.of(new Comment()));
        List<CommentDto> result = itemService.getCommentsByItem(1L);
        assertNotNull(result);
        assertEquals(1L, result.get(0).getId());

        verify(commentMapper).commentsToDto(any());
        verify(commentRepository).findAllByItemId(anyLong());

        verifyNoMoreInteractions(commentMapper);
        verifyNoMoreInteractions(commentRepository);
    }
}