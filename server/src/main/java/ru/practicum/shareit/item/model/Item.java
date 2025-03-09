package ru.practicum.shareit.item.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

@Entity
@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    @ManyToOne
    @JoinColumn(name = "owner_id")
    User owner;
    @Column(length = 100)
    String name;
    @Column(length = 300)
    String description;
    @Column(name = "is_available")
    boolean available;
    @ManyToOne
    @JoinColumn(name = "request_id")
    ItemRequest request;
}
