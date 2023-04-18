package fr.yncrea.cin3.shop.controller;

import fr.yncrea.cin3.shop.dto.BasketDto;
import fr.yncrea.cin3.shop.dto.BasketRequestDto;
import fr.yncrea.cin3.shop.service.BasketService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/basket")
public class BasketRestController {
    private BasketService service;
    public BasketRestController(BasketService service) {
        this.service = service;
    }

    @GetMapping
    public BasketDto get() {
        return service.get();
    }

    @PostMapping
    public void add(@Valid @RequestBody BasketRequestDto request) {
        service.add(request.getProductId());
    }

    @DeleteMapping
    public void remove(@Valid @RequestBody BasketRequestDto request) {
        service.remove(request.getProductId());
    }

    @PutMapping
    public void update(@Valid @RequestBody BasketRequestDto request) {
        service.update(request.getProductId(), request.getQuantity());
    }
}
