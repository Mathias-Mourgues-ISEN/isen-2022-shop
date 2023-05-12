package fr.yncrea.cin3.shop.service;

import fr.yncrea.cin3.shop.form.ProductForm;
import fr.yncrea.cin3.shop.model.Product;
import fr.yncrea.cin3.shop.repository.CategoryRepository;
import fr.yncrea.cin3.shop.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class ProductService {
    private ProductRepository products;

    private CategoryRepository categories;

    private Path uploadPath;

    public ProductService(ProductRepository products, CategoryRepository categories,
                          @Value("${application.uploads.products:var/uploads/products}") String uploadPath) {
        this.products = products;
        this.categories = categories;

        this.uploadPath = Paths.get(uploadPath).toAbsolutePath().normalize();

        // si le répertoire n'existe pas, on va le créer
        if (!this.uploadPath.toFile().isDirectory()) {
            this.uploadPath.toFile().mkdirs();
        }
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
        form.setPicturePresent(product.getPictureType() != null);

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

        // sauvegarde le type de la pièce jointe
        var picture = form.getPicture();
        if (!picture.isEmpty()) {
            // TODO Il faudrait vérifier que le type est autorisé (uniquement jpg/png par exemple)
            product.setPictureType(picture.getContentType());
        }

        // sauvegarde en base
       var result = products.save(product);

        // sauvegarde l'image si existante
        // à faire après le "save" pour déjà avoir l'id
        try {
            if (!picture.isEmpty()) {
                var target = Paths.get(uploadPath.toString(), result.getUuid().toString());
                picture.transferTo(target);
            }
        } catch (IOException e) {
            // TODO erreur lors de l'upload
        }

        return result;
    }

    @Transactional
    public Page<Product> findAll(Pageable pageable) {
        return products.findAllAndFetch(pageable);
    }

    public List<Product> findByCategory(UUID uuid) {
        return products.findByCategoryId(uuid);
    }

    public void remove(UUID id) {
        products.deleteById(id);
    }

    public ResponseEntity<FileSystemResource> getPictureAsResponseEntity(UUID uuid) {
        var match = products.findById(uuid);
        if (match.isEmpty() || match.get().getPictureType() == null)
            return ResponseEntity.notFound().build();

        var result = match.get();
        var target = Paths.get(uploadPath.toString(), result.getUuid().toString());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, result.getPictureType())
                .body(new FileSystemResource(target));
    }
}
