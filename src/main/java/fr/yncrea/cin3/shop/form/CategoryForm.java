package fr.yncrea.cin3.shop.form;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CategoryForm {
    private UUID uuid;

    @Size(min = 2, max = 40)
    private String name;
}
