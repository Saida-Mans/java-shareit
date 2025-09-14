package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TODO Sprint add-controllers.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {

    private Long id;

    @NotBlank(message = "Поле name не должно быть пустым")
    private String name;

    @NotBlank(message = "Описание не может быть пустым")
    private String description;

    private Boolean available;

    private Integer requestId;

    public ItemDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
