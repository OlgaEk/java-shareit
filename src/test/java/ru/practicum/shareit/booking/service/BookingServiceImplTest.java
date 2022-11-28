package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.dto.BookingBasicDataDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingMapper;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.AccessNotAllowed;
import ru.practicum.shareit.exception.NoEntityException;
import ru.practicum.shareit.exception.NotValidRequestException;
import ru.practicum.shareit.item.dto.ItemInfoDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserDto;
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
class BookingServiceImplTest {

    @Mock
    BookingRepository bookRepository;

    @Mock
    BookingMapper bookMapper;
    @Mock
    UserRepository userRepository;
    @Mock
    ItemRepository itemRepository;
    BookingRequestDto bookInDto;
    BookingResponseDto bookOutDto;
    Booking booking;
    Booking bookingResult;
    User user1;
    User user2;
    UserDto userDto;
    Item item;
    ItemInfoDto itemInfo;
    BookingBasicDataDto bookBase;
    PageRequest pageable;
    @InjectMocks
    private BookingServiceImpl bookingService;

    @BeforeEach
    void setUp() {
        user1 = new User();
        user1.setId(1L);

        user2 = new User();
        user2.setId(2L);

        userDto = new UserDto();
        userDto.setId(1L);

        item = new Item();
        item.setOwner(user2);

        itemInfo = new ItemInfoDto();
        itemInfo.setId(2L);
        itemInfo.setName("test");

        bookInDto = new BookingRequestDto();
        bookInDto.setItemId(1L);

        bookOutDto = new BookingResponseDto();
        bookOutDto.setBooker(userDto);
        bookOutDto.setStatus(BookingStatus.APPROVED);
        bookOutDto.setItem(itemInfo);

        booking = new Booking();
        booking.setBooker(user1);
        booking.setStatus(BookingStatus.WAITING);
        booking.setItem(item);

        bookingResult = new Booking();

        bookBase = new BookingBasicDataDto();
        bookBase.setId(1L);
        bookBase.setBookerId(1L);

        pageable = PageRequest.of(0, 10);
    }

    @Test
    void ShouldCreateBooking() {
        when(bookRepository.save(any())).thenReturn(bookingResult);
        when(bookMapper.requestDtoToBooking(any())).thenReturn(booking);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user1));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(bookMapper.bookingToResponseDto(any())).thenReturn(bookOutDto);

        BookingResponseDto result = bookingService.create(1L, bookInDto);
        assertNotNull(result);
        assertEquals(1L, result.getBooker().getId());
        assertEquals(itemInfo.getId(), result.getItem().getId());
        assertEquals(itemInfo.getName(), result.getItem().getName());

        verify(bookRepository).save(any());
        verify(bookMapper).requestDtoToBooking(any());
        verify(bookMapper).bookingToResponseDto(any());
        verify(userRepository).findById(1L);
        verify(itemRepository).findById(any());

        verifyNoMoreInteractions(bookRepository);
        verifyNoMoreInteractions(bookMapper);
        verifyNoMoreInteractions(userRepository);
        verifyNoMoreInteractions(itemRepository);
    }

    @Test
    void shouldThrowExceptionWhenCreateIfDataNotRight() {
        when(bookMapper.requestDtoToBooking(any())).thenReturn(booking);
        NoEntityException ex = assertThrows(NoEntityException.class,
                () -> bookingService.create(1L, bookInDto));
        assertEquals("User is not found", ex.getMessage());

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user1));
        ex = assertThrows(NoEntityException.class, () -> bookingService.create(1L, bookInDto));
        assertEquals("Item is not found", ex.getMessage());

        item.setOwner(user1);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        AccessNotAllowed exAccept = assertThrows(AccessNotAllowed.class,
                () -> bookingService.create(1l, bookInDto));
        assertEquals("Error: booker is owner of Item", exAccept.getMessage());

        verify(bookMapper, times(3)).requestDtoToBooking(any());
        verify(userRepository, times(3)).findById(anyLong());
        verify(itemRepository, times(2)).findById(anyLong());

    }

    @Test
    void shouldApproveOrRejectBooking() {
        when(bookRepository.findById(1L)).thenReturn(Optional.ofNullable(booking));
        when(bookRepository.save(any())).thenReturn(booking);
        when(bookMapper.bookingToResponseDto(any())).thenReturn(bookOutDto);
        item.setOwner(user1);

        BookingResponseDto result = bookingService.approve(1L, 1L, true);
        assertNotNull(result);
        assertEquals(BookingStatus.APPROVED, result.getStatus());

        booking.setStatus(BookingStatus.WAITING);
        result = bookingService.approve(1L, 1L, false);
        bookOutDto.setStatus(BookingStatus.REJECTED);
        assertNotNull(result);
        assertEquals(BookingStatus.REJECTED, result.getStatus());

        verify(bookRepository, times(2)).findById(1L);
        verify(bookRepository, times(2)).save(any());
        verify(bookMapper, times(2)).bookingToResponseDto(any());

        verifyNoMoreInteractions(bookRepository);
        verifyNoMoreInteractions(bookMapper);
    }

    @Test
    void shouldThrowExceptionWhenApproveWhenDataNotRight() {
        NoEntityException ex = assertThrows(NoEntityException.class,
                () -> bookingService.approve(1L, 1L, true));
        assertEquals("Booking is not found", ex.getMessage());

        when(bookRepository.findById(1L)).thenReturn(Optional.ofNullable(booking));
        AccessNotAllowed exAccess = assertThrows(AccessNotAllowed.class,
                () -> bookingService.approve(1L, 1L, true));
        assertEquals("User id = 1 is not owner of booking item", exAccess.getMessage());

        item.setOwner(user1);
        booking.setStatus(BookingStatus.APPROVED);
        NotValidRequestException exRequest = assertThrows(NotValidRequestException.class,
                () -> bookingService.approve(1L, 1L, true));

        verify(bookRepository, times(3)).findById(1L);

        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    void shouldGetBooking() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(bookMapper.bookingToResponseDto(any())).thenReturn(bookOutDto);

        BookingResponseDto result = bookingService.get(1L, 1L);
        assertNotNull(result);
        assertEquals(bookOutDto.getBooker(), result.getBooker());

        verify(bookRepository).findById(1L);
        verify(bookMapper).bookingToResponseDto(any());

        verifyNoMoreInteractions(bookRepository);
        verifyNoMoreInteractions(bookMapper);
    }

    @Test
    void shouldThrowExceptionWhenGetBookingIfDataNotRight() {
        NoEntityException ex = assertThrows(NoEntityException.class,
                () -> bookingService.get(1L, 1L));
        assertEquals("Booking is not found", ex.getMessage());

        booking.setBooker(user2);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(booking));
        AccessNotAllowed exAccess = assertThrows(AccessNotAllowed.class,
                () -> bookingService.get(1L, 1L));
        assertEquals("User id = 1 is not allowed to get this booking", exAccess.getMessage());

        verify(bookRepository, times(2)).findById(1L);

        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    void shouldGetAllBookingsByBooker() {
        when(bookMapper.bookingsToResponse(any())).thenReturn(List.of(bookOutDto));

        when(bookRepository.findAllByBookerIdOrderByStartDesc(1L, pageable))
                .thenReturn(List.of(bookingResult));
        List<BookingResponseDto> result = bookingService.getAllByBooker(1L, "ALL", pageable);
        assertEquals(1, result.size());

        when(bookRepository.findAllByBookerIdAndStatus(1L, BookingStatus.WAITING, pageable))
                .thenReturn(List.of(bookingResult));
        result = bookingService.getAllByBooker(1L, "WAITING", pageable);
        assertEquals(1, result.size());

        when(bookRepository.findAllByBookerIdAndStatus(1L, BookingStatus.REJECTED, pageable))
                .thenReturn(List.of(bookingResult));
        result = bookingService.getAllByBooker(1L, "REJECTED", pageable);
        assertEquals(1, result.size());

        when(bookRepository.findAllCurrentByBookerId(1L, LocalDateTime.now(), pageable))
                .thenReturn(List.of(bookingResult));
        result = bookingService.getAllByBooker(1L, "CURRENT", pageable);
        assertEquals(1, result.size());

        when(bookRepository.findAllPastByBookerId(1L, LocalDateTime.now(), pageable))
                .thenReturn(List.of(bookingResult));
        result = bookingService.getAllByBooker(1L, "PAST", pageable);
        assertEquals(1, result.size());

        when(bookRepository.findAllFutureByBookerId(1L, LocalDateTime.now(), pageable))
                .thenReturn(List.of(bookingResult));
        result = bookingService.getAllByBooker(1L, "FUTURE", pageable);
        assertEquals(1, result.size());

        verify(bookMapper, times(6)).bookingsToResponse(any());
        verify(bookRepository).findAllByBookerIdOrderByStartDesc(1L, pageable);
        verify(bookRepository).findAllByBookerIdAndStatus(1L, BookingStatus.WAITING, pageable);
        verify(bookRepository).findAllByBookerIdAndStatus(1L, BookingStatus.REJECTED, pageable);
        verify(bookRepository).findAllCurrentByBookerId(anyLong(), any(LocalDateTime.class), any(PageRequest.class));
        verify(bookRepository).findAllPastByBookerId(anyLong(), any(LocalDateTime.class), any(PageRequest.class));
        verify(bookRepository).findAllFutureByBookerId(anyLong(), any(LocalDateTime.class), any(PageRequest.class));

        verifyNoMoreInteractions(bookMapper);
        verifyNoMoreInteractions(bookRepository);

    }

    @Test
    void shouldGetAllBookingsByOwner() {
        when(bookMapper.bookingsToResponse(any())).thenReturn(List.of(bookOutDto));

        when(bookRepository.findAllByItemOwnerIdOrderByStartDesc(1L, pageable))
                .thenReturn(List.of(bookingResult));
        List<BookingResponseDto> result = bookingService.getAllByOwner(1L, "ALL", pageable);
        assertEquals(1, result.size());

        when(bookRepository.findAllByItemOwnerIdAndStatus(1L, BookingStatus.WAITING, pageable))
                .thenReturn(List.of(bookingResult));
        result = bookingService.getAllByOwner(1L, "WAITING", pageable);
        assertEquals(1, result.size());

        when(bookRepository.findAllByItemOwnerIdAndStatus(1L, BookingStatus.REJECTED, pageable))
                .thenReturn(List.of(bookingResult));
        result = bookingService.getAllByOwner(1L, "REJECTED", pageable);
        assertEquals(1, result.size());

        when(bookRepository.findAllCurrentByItemOwnerId(1L, LocalDateTime.now(), pageable))
                .thenReturn(List.of(bookingResult));
        result = bookingService.getAllByOwner(1L, "CURRENT", pageable);
        assertEquals(1, result.size());

        when(bookRepository.findAllPastByItemOwnerId(1L, LocalDateTime.now(), pageable))
                .thenReturn(List.of(bookingResult));
        result = bookingService.getAllByOwner(1L, "PAST", pageable);
        assertEquals(1, result.size());

        when(bookRepository.findAllFutureByItemOwnerId(1L, LocalDateTime.now(), pageable))
                .thenReturn(List.of(bookingResult));
        result = bookingService.getAllByOwner(1L, "FUTURE", pageable);
        assertEquals(1, result.size());

        verify(bookMapper, times(6)).bookingsToResponse(any());
        verify(bookRepository).findAllByItemOwnerIdOrderByStartDesc(1L, pageable);
        verify(bookRepository).findAllByItemOwnerIdAndStatus(1L, BookingStatus.WAITING, pageable);
        verify(bookRepository).findAllByItemOwnerIdAndStatus(1L, BookingStatus.REJECTED, pageable);
        verify(bookRepository).findAllCurrentByItemOwnerId(anyLong(), any(LocalDateTime.class), any(PageRequest.class));
        verify(bookRepository).findAllPastByItemOwnerId(anyLong(), any(LocalDateTime.class), any(PageRequest.class));
        verify(bookRepository).findAllFutureByItemOwnerId(anyLong(), any(LocalDateTime.class), any(PageRequest.class));

        verifyNoMoreInteractions(bookMapper);
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    void shouldThrowExceptionWhenGetAllBookingsByBookerOrOwnerIfStateNotRight() {
        NotValidRequestException ex = assertThrows(NotValidRequestException.class,
                () -> bookingService.getAllByBooker(1L, "test", pageable));
        assertEquals("Unknown state: test", ex.getMessage());

        ex = assertThrows(NotValidRequestException.class,
                () -> bookingService.getAllByOwner(1L, "test", pageable));
        assertEquals("Unknown state: test", ex.getMessage());
    }

    @Test
    void shouldGetLastBooking() {
        when(bookRepository.findItemLastBook(anyLong(), any(LocalDateTime.class))).thenReturn(List.of(booking));
        when(bookMapper.bookingToBasicDto(any(Booking.class))).thenReturn(bookBase);
        BookingBasicDataDto result = bookingService.getLastBooking(1L);
        assertNotNull(result);

        when(bookRepository.findItemLastBook(anyLong(), any(LocalDateTime.class))).thenReturn(List.of());
        result = bookingService.getLastBooking(1L);
        assertNull(result);

        verify(bookRepository, times(2)).findItemLastBook(anyLong(), any(LocalDateTime.class));
        verify(bookMapper).bookingToBasicDto(any(Booking.class));

        verifyNoMoreInteractions(bookRepository);
        verifyNoMoreInteractions(bookMapper);
    }

    @Test
    void getNextBooking() {
        when(bookRepository.findItemNextBook(anyLong(), any(LocalDateTime.class))).thenReturn(List.of(booking));
        when(bookMapper.bookingToBasicDto(any(Booking.class))).thenReturn(bookBase);
        BookingBasicDataDto result = bookingService.getNextBooking(1L);
        assertNotNull(result);
        assertEquals(bookBase.getBookerId(), result.getBookerId());
        assertEquals(bookBase.getId(), result.getId());

        when(bookRepository.findItemNextBook(anyLong(), any(LocalDateTime.class))).thenReturn(List.of());
        result = bookingService.getNextBooking(1L);
        assertNull(result);

        verify(bookRepository, times(2)).findItemNextBook(anyLong(), any(LocalDateTime.class));
        verify(bookMapper).bookingToBasicDto(any(Booking.class));

        verifyNoMoreInteractions(bookRepository);
        verifyNoMoreInteractions(bookMapper);
    }
}