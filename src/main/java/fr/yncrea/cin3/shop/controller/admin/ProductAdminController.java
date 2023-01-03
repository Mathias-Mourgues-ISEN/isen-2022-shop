package fr.yncrea.cin3.shop.controller.admin;

import fr.yncrea.cin3.shop.form.ProductForm;
import fr.yncrea.cin3.shop.service.CategoryService;
import fr.yncrea.cin3.shop.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/admin/product")
public class ProductAdminController {

    private ProductService service;
    private CategoryService categoryService;

    public ProductAdminController(ProductService service, CategoryService categoryService) {
        this.service = service;
        this.categoryService = categoryService;
    }

    @GetMapping({"/create", "/update/{id}"})
    public String save(@PathVariable(required = false) UUID id, Model model) {
        model.addAttribute("form", service.initFormFromUUID(id));
        model.addAttribute("categories", categoryService.findAll());

        return "admin/product/save";
    }

    @PostMapping("/save")
    public String saveAction(@ModelAttribute("form") ProductForm form, BindingResult result, Model model) {
        // en cas d'erreur, on ré-affiche le formulaire
        if (result.hasErrors()) {
            model.addAttribute("form", form);
            model.addAttribute("categories", categoryService.findAll());
            return "admin/product/save";
        }

        service.update(form);

        return "redirect:/admin/product";
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("objects", service.findAll());

        return "admin/product/list";
    }
}
