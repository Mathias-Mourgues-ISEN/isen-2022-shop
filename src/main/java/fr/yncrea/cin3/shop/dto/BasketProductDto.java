package fr.yncrea.cin3.shop.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
public class BasketProductDto implements Serializable {
    private UUID productId;
    private String productName;
    private Integer quantity;
    private Integer price;
}
