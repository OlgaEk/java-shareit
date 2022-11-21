package ru.practicum.shareit.booking.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByBookerIdOrderByStartDesc(Long bookerId);


    @Query("select b from Booking b where b.booker.id = ?1 and b.start < ?2 and b.end > ?2 order by b.start DESC")
    List<Booking> findAllCurrentByBookerId(Long id, LocalDateTime now);

    @Query("select b from Booking b where b.booker.id = ?1 and b.end < ?2 order by b.start DESC")
    List<Booking> findAllPastByBookerId(Long id, LocalDateTime end);

    @Query("select b from Booking b where b.booker.id = ?1 and b.start > ?2 order by b.start DESC")
    List<Booking> findAllFutureByBookerId(Long id, LocalDateTime start);

    List<Booking> findAllByItemOwnerIdOrderByStartDesc(Long userId);

    @Query("select b from Booking b where b.item.owner.id = ?1 and b.start < ?2 and b.end > ?2 order by b.start DESC")
    List<Booking> findAllCurrentByItemOwnerId(Long id, LocalDateTime now);

    @Query("select b from Booking b where b.item.owner.id = ?1 and b.end < ?2 order by b.start DESC")
    List<Booking> findAllPastByItemOwnerId(Long id, LocalDateTime end);

    @Query("select b from Booking b where b.item.owner.id = ?1 and b.start > ?2 order by b.start DESC")
    List<Booking> findAllFutureByItemOwnerId(Long id, LocalDateTime start);

    @Query("select b from Booking b where b.booker.id = ?1 and b.status = ?2 order by b.start DESC")
    List<Booking> findAllByBookerIdAndStatus(Long id, BookingStatus status);

    @Query("select b from Booking b where b.item.owner.id = ?1 and b.status = ?2 order by b.start DESC")
    List<Booking> findAllByItemOwnerIdAndStatus(Long id, BookingStatus status);

    @Query("select b from Booking b where b.item.id = ?1 and b.end < ?2 order by b.end DESC")
    List<Booking> findItemLastBook(Long itemId, LocalDateTime end);

    @Query("select b from Booking b where b.item.id = ?1 and b.start > ?2 order by b.start")
    List<Booking> findItemNextBook(Long itemId, LocalDateTime start);

    @Query("select b from Booking b where b.booker.id = ?1 and b.item.id = ?2 and b.status = 'APPROVED' and b.end < ?3")
    List<Booking> findAllByBookerIdAndItemId(Long bookerId, Long itemId, LocalDateTime now);

    @Query("select b from Booking b where b.item.id = ?1")
    List<Booking> findAllByItemId(Long id);


}
