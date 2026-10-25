package com.mall.mallapp.repository;

import com.mall.mallapp.model.Sale;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleRepository extends JpaRepository<Sale, Long> {
}