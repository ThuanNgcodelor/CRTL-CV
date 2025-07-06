package com.example.stockservice.service.product;

import com.example.stockservice.model.Product;
import com.example.stockservice.request.product.ProductCreateRequest;
import com.example.stockservice.request.product.ProductUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    Product createProduct(ProductCreateRequest request, MultipartFile multipartFile);
    Product updateProduct(ProductUpdateRequest request, MultipartFile multipartFile);
    Product getProductById(String id);
    Product findProductById(String id);
    void deleteProduct(String id);
    Page<Product> getAllProducts(Integer pageNo);
    Page<Product> searchProductByKeyword(String keyword, Integer pageNo);
    List<Product> getAllProducts();
}
