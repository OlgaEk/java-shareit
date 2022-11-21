package ru.practicum.shareit.booking.model;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * // This class describes the entity of Booking
 */
@Entity
@Getter
@Setter
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "start_date")
    private LocalDateTime start;
    @Column(name = "end_date")
    private LocalDateTime end;
    @ManyToOne(optional = false)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;
    //    Разобралась. Почитала про связь, удивилась, что программа прошла проверки,
//    так как явно создавались не один запрос к вещи и база данных должна была выдать исключение.
    @ManyToOne(optional = false)
    @JoinColumn(name = "booker_id", nullable = false)
    private User booker;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private BookingStatus status = BookingStatus.WAITING;
}
