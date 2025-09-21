package ru.practicum.shareit.request.module;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import ru.practicum.shareit.item.module.Item;

@Entity
@Table(name = "requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_Id", nullable = false)
    private Long userId;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "description_time", nullable = false)
    private LocalDateTime created;

    @OneToMany(mappedBy = "request")
    private List<Item> items = new ArrayList<>();
}
