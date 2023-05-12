package fr.yncrea.cin3.shop.repository;

import fr.yncrea.cin3.shop.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    @Query("SELECT p from Product p WHERE p.category.id = :uuid")
    List<Product> findByCategoryId(UUID uuid);

    @Query("SELECT p.id from Product p WHERE p.category.id = :uuid")
    List<UUID> findIdsByCategoryId(UUID uuid);

    @Query(value = "SELECT p FROM Product p JOIN FETCH p.category",
            countQuery = "SELECT COUNT(p.id) FROM Product p")
    Page<Product> findAllAndFetch(Pageable page);
}
