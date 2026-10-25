package com.mall.mallapp.controller;

import com.mall.mallapp.model.*;
import com.mall.mallapp.repository.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.*;

@Controller
public class ReportController {

    private static final int LOW_STOCK_LIMIT = 5;

    private final ProductRepository productRepo;
    private final SaleRepository saleRepo;

    public ReportController(ProductRepository productRepo, SaleRepository saleRepo) {
        this.productRepo = productRepo;
        this.saleRepo = saleRepo;
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/products";
    }

    @GetMapping("/sales")
    public String sales(Model model) {
        List<Sale> sales = new ArrayList<>(saleRepo.findAll());
        sales.sort((a, b) -> Long.compare(b.getId(), a.getId()));   // newest first
        model.addAttribute("sales", sales);
        return "sales";
    }

    @GetMapping("/reports")
    public String reports(Model model) {
        List<Sale> sales = saleRepo.findAll();

        double revenue = 0;
        double gstCollected = 0;
        Map<String, Integer> qtySold = new HashMap<>();

        for (Sale s : sales) {
            revenue += s.getTotal();
            gstCollected += s.getGst();
            for (SaleItem item : s.getItems()) {
                qtySold.merge(item.getProductName(), item.getQty(), Integer::sum);
            }
        }

        String bestItem = "-";
        int bestQty = 0;
        for (Map.Entry<String, Integer> e : qtySold.entrySet()) {
            if (e.getValue() > bestQty) {
                bestQty = e.getValue();
                bestItem = e.getKey();
            }
        }

        double average = sales.isEmpty() ? 0 : revenue / sales.size();

        model.addAttribute("totalBills", sales.size());
        model.addAttribute("revenue", revenue);
        model.addAttribute("gstCollected", gstCollected);
        model.addAttribute("average", average);
        model.addAttribute("bestItem", bestItem);
        model.addAttribute("bestQty", bestQty);
        model.addAttribute("totalProducts", productRepo.count());
        model.addAttribute("lowStock",
                productRepo.findAll().stream().filter(p -> p.getStock() <= LOW_STOCK_LIMIT).toList());
        model.addAttribute("limit", LOW_STOCK_LIMIT);
        return "reports";
    }
}