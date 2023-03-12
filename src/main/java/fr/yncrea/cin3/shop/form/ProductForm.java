package fr.yncrea.cin3.shop.form;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ProductForm {
    private UUID uuid;

    @Size(min = 2, max = 60)
    private String name;

    private String description;

    @Min(0)
    @NotNull
    private Double price;

    @NotNull
    private UUID category;
}
