package fr.yncrea.cin3.shop.form;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.util.UUID;

@Getter
@Setter
public class ProductForm {
    private UUID uuid;

    private String name;

    private String description;

    private Double price;

    private UUID category;
}
