package fr.yncrea.cin3.shop.controller;

import fr.yncrea.cin3.shop.service.CategoryService;
import fr.yncrea.cin3.shop.service.ProductService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Locale;
import java.util.UUID;

@Controller
public class IndexController {
    private CategoryService categoryService;
    private ProductService productService;

    public IndexController(CategoryService categoryService, ProductService productService) {
        this.categoryService = categoryService;
        this.productService = productService;
    }

    @GetMapping({"", "/"})
    public String index(Model model) {
        model.addAttribute("categories", categoryService.findAll());

        return "index";
    }

    @GetMapping("/category/{uuid}")
    public String products(Model model, @PathVariable UUID uuid, Locale locale) {
        model.addAttribute("category", categoryService.findById(uuid));
        model.addAttribute("products", productService.findByCategory(uuid));

        return "products-by-category";
    }

    @GetMapping("product/{uuid}/picture")
    public ResponseEntity<FileSystemResource> productPicture(@PathVariable UUID uuid) {
        return productService.getPictureAsResponseEntity(uuid);
    }
}
