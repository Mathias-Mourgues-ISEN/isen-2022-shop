package fr.yncrea.cin3.shop.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
public class Product {
    @Id
    @GeneratedValue
    private UUID uuid;

    private String name;

    private String description;

    private Integer price;

    @ManyToOne
    private Category category;
}
