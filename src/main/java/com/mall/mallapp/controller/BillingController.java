package com.mall.mallapp.controller;

import com.mall.mallapp.model.*;
import com.mall.mallapp.repository.*;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@Controller
public class BillingController {

    private static final double GST_RATE = 0.18;

    private final ProductRepository productRepo;
    private final SaleRepository saleRepo;

    public BillingController(ProductRepository productRepo, SaleRepository saleRepo) {
        this.productRepo = productRepo;
        this.saleRepo = saleRepo;
    }

    @GetMapping("/billing")
    public String billingPage(Model model) {
        model.addAttribute("products", productRepo.findAll());
        return "billing";
    }

    @PostMapping("/billing")
    @Transactional
    public String createBill(@RequestParam("customerName") String customerName,
                             @RequestParam Map<String, String> params,
                             Model model) {

        List<SaleItem> items = new ArrayList<>();
        double subtotal = 0;

        // Pass 1: check everything first, change nothing yet
        for (Product p : productRepo.findAll()) {
            String value = params.get("qty_" + p.getId());
            if (value == null || value.isBlank()) continue;
            int qty = Integer.parseInt(value);
            if (qty <= 0) continue;

            if (qty > p.getStock()) {
                model.addAttribute("products", productRepo.findAll());
                model.addAttribute("error",
                        "Not enough stock for " + p.getName() + " (available: " + p.getStock() + ")");
                return "billing";
            }

            SaleItem item = new SaleItem();
            item.setProductId(p.getId());
            item.setProductName(p.getName());
            item.setPrice(p.getPrice());
            item.setQty(qty);
            item.setLineTotal(p.getPrice() * qty);
            items.add(item);
            subtotal += item.getLineTotal();
        }

        if (items.isEmpty()) {
            model.addAttribute("products", productRepo.findAll());
            model.addAttribute("error", "Please enter a quantity for at least one product.");
            return "billing";
        }

        // Pass 2: everything is fine, so reduce the stock
        for (SaleItem item : items) {
            Product p = productRepo.findById(item.getProductId()).orElseThrow();
            p.setStock(p.getStock() - item.getQty());
            productRepo.save(p);
        }

        Sale sale = new Sale();
        sale.setCustomerName(customerName.isBlank() ? "Walk-in Customer" : customerName);
        sale.setDateTime(LocalDateTime.now());
        sale.setItems(items);
        sale.setSubtotal(round(subtotal));
        sale.setGst(round(subtotal * GST_RATE));
        sale.setTotal(round(subtotal + subtotal * GST_RATE));

        Sale saved = saleRepo.save(sale);
        return "redirect:/bill/" + saved.getId();
    }

    @GetMapping("/bill/{id}")
    public String showBill(@PathVariable("id") Long id, Model model) {
        model.addAttribute("sale", saleRepo.findById(id).orElseThrow());
        return "bill";
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}