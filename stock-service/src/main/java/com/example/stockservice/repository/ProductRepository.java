package com.example.stockservice.repository;

import com.example.stockservice.emuns.ProductStatus;
import com.example.stockservice.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,String> {
    @Query("select p from products p where p.name like %?1%")
    List<Product> searchProductByName(String name);
    Page<Product> findAllByStatus(ProductStatus status, Pageable pageable);
}
