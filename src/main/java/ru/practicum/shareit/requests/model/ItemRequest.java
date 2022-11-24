package ru.practicum.shareit.requests.model;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * // This class describes the entity of Request, that save in base
 */


@Entity
@Getter
@Setter
@Table(name = "requests")
public class ItemRequest {
    @OneToMany
    @JoinColumn(name = "request_id")
    List<Item> itemsOnRequest;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String description;
    @Column
    private LocalDateTime created;
    @ManyToOne(optional = false)
    @JoinColumn(name = "requester_id", nullable = false)
    private User requester;
}
