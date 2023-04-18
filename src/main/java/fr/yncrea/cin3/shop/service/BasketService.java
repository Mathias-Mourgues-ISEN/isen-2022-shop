package fr.yncrea.cin3.shop.service;

import fr.yncrea.cin3.shop.dto.BasketDto;
import fr.yncrea.cin3.shop.dto.BasketProductDto;
import fr.yncrea.cin3.shop.dto.SessionDto;
import fr.yncrea.cin3.shop.model.Product;
import fr.yncrea.cin3.shop.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.Objects;
import java.util.UUID;

@Service
public class BasketService {
    private SessionDto session;
    private ProductRepository productRepository;

    public BasketService(SessionDto session, ProductRepository productRepository) {
        this.session = session;
        this.productRepository = productRepository;
    }

    public BasketDto get() {
        return session.getBasket();
    }

    private void updatePrice(BasketDto basket) {
        LinkedList<BasketProductDto> toRemove = new LinkedList<>();

        // calcule le prix
        int price = 0;
        for (BasketProductDto productDto: basket.getProducts()) {
            if (productDto.getQuantity() <= 0) {
                toRemove.add(productDto);
            } else {
                price += productDto.getPrice() * productDto.getQuantity();
            }
        }

        // supprime les produits absents
        basket.getProducts().removeAll(toRemove);

        // met à jour le prix
        basket.setPrice(price);
    }

    public void add(UUID productId) {
        BasketDto basket = session.getBasket();

        // recherche le produit en base
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Le produit n'existe pas"));

        // si le produit existe déjà dans le panier
        boolean found = false;
        for (BasketProductDto productDto: basket.getProducts()) {
            if (Objects.equals(productDto.getProductId(), product.getUuid())) {
                found = true;

                // ajoute 1 à la quantité
                var quantity = productDto.getQuantity() + 1;
                productDto.setQuantity(quantity);
            }
        }

        // sinon, le rajoute
        if (found == false) {
            BasketProductDto productDto = new BasketProductDto();
            productDto.setProductId(product.getUuid());
            productDto.setProductName(product.getName());
            productDto.setPrice(product.getPrice());
            productDto.setQuantity(1);

            basket.getProducts().add(productDto);
        }

        updatePrice(basket);
    }

    public void remove(UUID productId) {
        BasketDto basket = session.getBasket();

        // recherche le produit en base
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Le produit n'existe pas"));

        // si le produit existe déjà dans le panier
        for (BasketProductDto productDto : basket.getProducts()) {
            if (Objects.equals(productDto.getProductId(), product.getUuid())) {
                // supprime 1 à la quantité
                var quantity = productDto.getQuantity() - 1;
                productDto.setQuantity(quantity);
            }
        }

        updatePrice(basket);
    }

    public void update(UUID productId, Integer quantity) {
        BasketDto basket = session.getBasket();

        // recherche le produit en base
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Le produit n'existe pas"));

        // si le produit existe déjà dans le panier
        for (BasketProductDto productDto : basket.getProducts()) {
            if (Objects.equals(productDto.getProductId(), product.getUuid())) {
                // défini la quantité
                productDto.setQuantity(quantity);
            }
        }

        updatePrice(basket);
    }
}
