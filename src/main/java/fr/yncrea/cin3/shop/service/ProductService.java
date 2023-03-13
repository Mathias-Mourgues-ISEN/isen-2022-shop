package fr.yncrea.cin3.shop.service;

import fr.yncrea.cin3.shop.form.ProductForm;
import fr.yncrea.cin3.shop.model.Product;
import fr.yncrea.cin3.shop.repository.CategoryRepository;
import fr.yncrea.cin3.shop.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProductService {
    private ProductRepository products;

    private CategoryRepository categories;

    public ProductService(ProductRepository products, CategoryRepository categories) {
        this.products = products;
        this.categories = categories;
    }

    @Transactional
    public ProductForm initFormFromUUID(UUID id) {
        // initialise un formulaire vide
        var form = new ProductForm();

        // s'il n'y a pas d'id, pas d'initialisation à faire
        if (id == null)
            return form;

        // recherche la catégorie en base
        var product = products.findById(id).orElseThrow(() -> new RuntimeException("Le produit n'existe pas"));

        form.setUuid(product.getUuid());
        form.setName(product.getName());
        form.setDescription(product.getDescription());
        form.setPrice(product.getPrice() / 1000.0);
        form.setCategory(product.getCategory().getUuid());

        return form;
    }

    @Transactional
    public Product update(ProductForm form) {
        // crée une nouvelle catégorie
        var product = new Product();

        // si on a un uuid précédent, c'est un update
        if (form.getUuid() != null) {
            product = products.findById(form.getUuid()).orElseThrow(() -> new RuntimeException("Le produit n'existe pas"));
        }

        // copie les attributs
        product.setName(form.getName());
        product.setDescription(form.getDescription());

        // conversion du prix
        var price = (int) (1000 * form.getPrice());
        product.setPrice(price);

        var category = categories.findById(form.getCategory()).orElseThrow(() -> new RuntimeException("La catégorie n'existe pas"));
        product.setCategory(category);

        // sauvegarde en base
        return products.save(product);
    }

    @Transactional
    public List<Product> findAll() {
        return products.findAll();
    }

    public List<Product> findByCategory(UUID uuid) {
        return products.findByCategoryId(uuid);
    }

    public void remove(UUID id) {
        products.deleteById(id);
    }
}
