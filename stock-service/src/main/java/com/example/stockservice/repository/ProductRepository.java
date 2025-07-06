package com.example.stockservice.repository;

import com.example.stockservice.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,String> {
    @Query("select p from products p where p.name like %?1%")
    List<Product> searchProductByName(String name);
}
