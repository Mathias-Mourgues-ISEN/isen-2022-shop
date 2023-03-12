package fr.yncrea.cin3.shop.controller.admin;

import fr.yncrea.cin3.shop.form.CategoryForm;
import fr.yncrea.cin3.shop.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/admin/category")
public class CategoryAdminController {
    private CategoryService service;

    public CategoryAdminController(CategoryService service) {
        this.service = service;
    }

    @GetMapping({"/create", "/update/{id}"})
    public String save(@PathVariable(required = false) UUID id, Model model) {
        model.addAttribute("form", service.initFormFromUUID(id));

        return "admin/category/save";
    }

    @PostMapping("/save")
    public String saveAction(@Valid @ModelAttribute("form") CategoryForm form, BindingResult result, Model model) {
        // en cas d'erreur, on ré-affiche le formulaire
        if (result.hasErrors()) {
            model.addAttribute("form", form);
            return "admin/category/save";
        }

        service.update(form);

        return "redirect:/admin/category";
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("objects", service.findAll());

        return "admin/category/list";
    }
}
