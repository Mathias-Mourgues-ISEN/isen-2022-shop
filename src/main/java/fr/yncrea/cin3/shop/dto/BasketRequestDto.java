package fr.yncrea.cin3.shop.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class BasketRequestDto {
    @NotNull
    private UUID productId;

    @Min(0)
    private Integer quantity;
}
