package ru.practicum.shareit.item.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.model.User;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "items")
@EqualsAndHashCode(of = "id")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    @ManyToOne
    @JoinColumn(name = "is_available")
    User owner;
    @Column(length = 100)
    String name;
    @Column(length = 300)
    String description;
    @Column(name = "is_available")
    Boolean available;
    @OneToOne
    @JoinColumn(name = "request_id")
    ItemRequest request;
}
