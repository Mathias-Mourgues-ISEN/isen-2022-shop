package fr.yncrea.cin3.shop.service;

import fr.yncrea.cin3.shop.form.CategoryForm;
import fr.yncrea.cin3.shop.model.Category;
import fr.yncrea.cin3.shop.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CategoryService {
    private CategoryRepository categories;

    public CategoryService(CategoryRepository categories) {
        this.categories = categories;
    }

    @Transactional
    public CategoryForm initFormFromUUID(UUID id) {
        // initialise un formulaire vide
        var form = new CategoryForm();

        // s'il n'y a pas d'id, pas d'initialisation à faire
        if (id == null)
            return form;

        // recherche la catégorie en base
        var category = categories.findById(id).orElseThrow(() -> new RuntimeException("La catégorie n'existe pas"));

        form.setUuid(category.getUuid());
        form.setName(category.getName());

        return form;
    }

    @Transactional
    public Category update(CategoryForm form) {
        // crée une nouvelle catégorie
        var category = new Category();

        // si on a un uuid précédent, c'est un update
        if (form.getUuid() != null) {
            category = categories.findById(form.getUuid()).orElseThrow(() -> new RuntimeException("La catégorie n'existe pas"));
        }

        // copie les attributs
        category.setName(form.getName());

        // sauvegarde en base
        return categories.save(category);
    }

    @Transactional
    public List<Category> findAll() {
        return categories.findAll();
    }
}
