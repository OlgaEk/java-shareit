package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.controller.State;
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
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingMapper bookingMapper;

    @Transactional
    public BookingResponseDto create(Long bookerId, BookingRequestDto bookingRequestDto) {
        Booking booking = bookingMapper.requestDtoToBooking(bookingRequestDto);
        booking.setBooker(userRepository.findById(bookerId)
                .orElseThrow(() -> new NoEntityException("User is not found")));
        booking.setItem(itemRepository.findById(bookingRequestDto.getItemId())
                .orElseThrow(() -> new NoEntityException("Item is not found")));
        if (Objects.equals(booking.getItem().getOwner().getId(), bookerId))
            throw new AccessNotAllowed("Error: booker is owner of Item");
        return bookingMapper.bookingToResponseDto(bookingRepository.save(booking));
    }

    @Transactional
    public BookingResponseDto approve(Long userId, Long bookingId, boolean approved) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NoEntityException("Booking is not found"));
        if (!Objects.equals(booking.getItem().getOwner().getId(), userId))
            throw new AccessNotAllowed("User id = " + userId + "is not owner of booking item");
        if (booking.getStatus() == BookingStatus.WAITING) {
            if (approved) booking.setStatus(BookingStatus.APPROVED);
            else booking.setStatus(BookingStatus.REJECTED);
        } else throw new NotValidRequestException("Status of booking already changed");
        return bookingMapper.bookingToResponseDto(bookingRepository.save(booking));
    }

    public BookingResponseDto get(Long userId, Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NoEntityException("Booking is not found"));
        if (Objects.equals(booking.getItem().getOwner().getId(), userId) ||
                Objects.equals(booking.getBooker().getId(), userId))
            return bookingMapper.bookingToResponseDto(booking);
        else throw new AccessNotAllowed("User id = " + userId + " is not allowed to get this booking");
    }

    public List<BookingResponseDto> getAllByBooker(Long userId, String state) {
        State currentState = convertToState(state);
        switch (currentState) {
            case ALL:
                return bookingMapper.bookingsToResponse(
                        bookingRepository.findAllByBookerIdOrderByStartDesc(userId));
            case WAITING:
            case REJECTED:
                return bookingMapper.bookingsToResponse(
                        bookingRepository.findAllByBookerIdAndStatus(userId,
                                BookingStatus.valueOf(currentState.toString())));
            case CURRENT:
                return bookingMapper.bookingsToResponse(
                        bookingRepository.findAllCurrentByBookerId(userId, LocalDateTime.now()));
            case PAST:
                return bookingMapper.bookingsToResponse(
                        bookingRepository.findAllPastByBookerId(userId, LocalDateTime.now()));
            case FUTURE:
                return bookingMapper.bookingsToResponse(
                        bookingRepository.findAllFutureByBookerId(userId, LocalDateTime.now()));
            default:
                throw new NotValidRequestException("State is not use");
        }
    }

    public List<BookingResponseDto> getAllByOwner(Long userId, String state) {
        State currentState = convertToState(state);
        switch (currentState) {
            case ALL:
                return bookingMapper.bookingsToResponse(
                        bookingRepository.findAllByItemOwnerIdOrderByStartDesc(userId));
            case WAITING:
            case REJECTED:
                return bookingMapper.bookingsToResponse(
                        bookingRepository.findAllByItemOwnerIdAndStatus(userId,
                                BookingStatus.valueOf(currentState.toString())));
            case CURRENT:
                return bookingMapper.bookingsToResponse(
                        bookingRepository.findAllCurrentByItemOwnerId(userId, LocalDateTime.now()));
            case PAST:
                return bookingMapper.bookingsToResponse(
                        bookingRepository.findAllPastByItemOwnerId(userId, LocalDateTime.now()));
            case FUTURE:
                return bookingMapper.bookingsToResponse(
                        bookingRepository.findAllFutureByItemOwnerId(userId, LocalDateTime.now()));
            default:
                throw new NotValidRequestException("State is not use");
        }

    }

    private State convertToState(String state) {
        try {
            return State.valueOf(state.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new NotValidRequestException(String.format("Unknown state: %s", state));
        }
    }

    public BookingBasicDataDto getLastBooking(Long itemId) {
        //Как я заметила на тестах вариант с отбором последнего и ближайшего бронирования работает медленно
        //Вариант замены:

       /* List<Booking> booking = bookingRepository.findAllByItemId(itemId).stream()
                .filter(b -> b.getEnd().isBefore(LocalDateTime.now()))
                .sorted(Comparator.comparing(Booking::getEnd).reversed())
                .collect(Collectors.toList());*/

        List<Booking> booking = bookingRepository.findItemLastBook(itemId, LocalDateTime.now());
        if (booking.isEmpty())
            return null;
        BookingBasicDataDto book = bookingMapper.bookingToBasicDto(booking.get(0));
        return book;
    }

    public BookingBasicDataDto getNextBooking(Long itemId) {
       /* List<Booking> booking = bookingRepository.findAllByItemId(itemId).stream()
                .filter(b -> b.getStart().isAfter(LocalDateTime.now()))
                .sorted(Comparator.comparing(Booking::getStart))
                .collect(Collectors.toList());*/

        List<Booking> booking = bookingRepository.findItemNextBook(itemId, LocalDateTime.now());
        if (booking.isEmpty())
            return null;
        return bookingMapper.bookingToBasicDto(booking.get(0));
    }

}
