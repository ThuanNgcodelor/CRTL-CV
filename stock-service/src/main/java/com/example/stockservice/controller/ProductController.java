package com.example.stockservice.controller;

import com.example.stockservice.dto.ProductDto;
import com.example.stockservice.model.Product;
import com.example.stockservice.request.product.DecreaseStockRequest;
import com.example.stockservice.request.product.ProductCreateRequest;
import com.example.stockservice.request.product.ProductUpdateRequest;
import com.example.stockservice.service.product.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RequestMapping("/v1/stock/product")
@RequiredArgsConstructor
@RestController
public class ProductController {
    private final ProductService productService;
    private final ModelMapper modelMapper;

    @PostMapping("/decreaseStock")
    public ResponseEntity<ProductDto> decreaseStock(@RequestBody DecreaseStockRequest request) {
        productService.decreaseStock(request.getProductId(), request.getQuantity());

        ProductDto productDto = modelMapper.map(
                productService.getProductById(request.getProductId()), ProductDto.class);
        return ResponseEntity.status(HttpStatus.OK).body(productDto);
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<ProductDto> createProduct(@Valid @RequestPart ProductCreateRequest request,
                                              @RequestPart(required = false) MultipartFile file) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(modelMapper.map(productService.createProduct(request,file), ProductDto.class));
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<ProductDto> updateProduct(@Valid @RequestPart ProductUpdateRequest request,
                                             @RequestPart(required = false) MultipartFile file) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(modelMapper.map(productService.updateProduct(request, file), ProductDto.class));
    }

    @DeleteMapping("/deleteProductById/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<ProductDto> deleteProductById(@PathVariable String id) {
        productService.deleteProduct(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/getProductById/{id}")
    ResponseEntity<ProductDto> getProductById(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(modelMapper.map(productService.getProductById(id), ProductDto.class));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ProductDto>> searchProducts(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "1") Integer pageNo) {

        Page<Product> products = productService.searchProductByKeyword(keyword, pageNo);
        Page<ProductDto> dtoPage = products.map(product -> modelMapper.map(product, ProductDto.class));
        return ResponseEntity.ok(dtoPage);
    }

    @GetMapping("/list")
    ResponseEntity<Page<ProductDto>> getAllProducts(@RequestParam(defaultValue = "1") Integer pageNo) {
        Page<ProductDto> products = productService.getAllProducts(pageNo).map(product -> modelMapper.map(product, ProductDto.class));
        return ResponseEntity.status(HttpStatus.OK).body(products);
    }
}
