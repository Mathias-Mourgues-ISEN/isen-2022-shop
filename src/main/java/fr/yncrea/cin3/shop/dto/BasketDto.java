package fr.yncrea.cin3.shop.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.LinkedList;

@Getter
@Setter
public class BasketDto implements Serializable {
    private Integer price = 0;

    private LinkedList<BasketProductDto> products = new LinkedList<>();
}
