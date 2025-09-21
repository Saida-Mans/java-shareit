package ru.practicum.shareit.item.module;
import jakarta.persistence.*;
import lombok.*;
import ru.practicum.shareit.booking.module.Booking;
import ru.practicum.shareit.request.module.Request;
import ru.practicum.shareit.user.module.User;


import java.util.List;

/**
 * TODO Sprint add-controllers.
 */

@Entity
@Table(name = "items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "available")
    private boolean isAvailable;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @OneToMany(mappedBy = "item")
    private List<Booking> bookings;

    @OneToMany(mappedBy = "item")
    private List<Comment> comments;

    @ManyToOne
    @JoinColumn(name = "request_id")
    private Request request;
}
