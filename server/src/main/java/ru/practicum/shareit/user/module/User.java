package ru.practicum.shareit.user.module;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.shareit.booking.module.Booking;
import ru.practicum.shareit.item.module.Comment;
import ru.practicum.shareit.item.module.Item;


import java.util.List;

/**
 * TODO Sprint add-controllers.
 */

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false)
    private String email;

    @OneToMany(mappedBy = "owner")
    private List<Item> items;

    @OneToMany(mappedBy = "booker")
    private List<Booking> bookings;

    @OneToMany(mappedBy = "author")
    private List<Comment> comment;
}
