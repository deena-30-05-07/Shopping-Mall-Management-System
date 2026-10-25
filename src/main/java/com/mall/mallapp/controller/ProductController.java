package com.mall.mallapp.controller;

import com.mall.mallapp.model.Product;
import com.mall.mallapp.repository.ProductRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ProductController {

    private final ProductRepository repo;

    public ProductController(ProductRepository repo) {
        this.repo = repo;
    }

    @GetMapping("/products")
    public String list(Model model) {
        model.addAttribute("products", repo.findAll());
        model.addAttribute("product", new Product());
        return "products";
    }

    @PostMapping("/products")
    public String add(@ModelAttribute Product product) {
        repo.save(product);
        return "redirect:/products";
    }

    @PostMapping("/products/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        repo.deleteById(id);
        return "redirect:/products";
    }

    @PostMapping("/products/restock/{id}")
    public String restock(@PathVariable("id") Long id, @RequestParam("qty") int qty) {
        Product p = repo.findById(id).orElseThrow();
        p.setStock(p.getStock() + qty);
        repo.save(p);
        return "redirect:/products";
    }
}