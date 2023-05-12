package fr.yncrea.cin3.shop.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.io.Serializable;

@Getter
@Setter
@Component
@SessionScope
public class SessionDto implements Serializable {
    private BasketDto basket = new BasketDto();
}
