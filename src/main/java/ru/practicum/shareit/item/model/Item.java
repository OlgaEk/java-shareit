package ru.practicum.shareit.item.model;


import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;

/**
 * // This class describes the entity of Item, that save in base
 */
@Entity
@Getter
@Setter
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String name;
    @Column
    private String description;
    @Column(name = "is_available")
    private boolean available;
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;
}