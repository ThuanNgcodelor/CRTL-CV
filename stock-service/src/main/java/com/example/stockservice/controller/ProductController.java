package com.example.stockservice.controller;

import com.example.stockservice.dto.CategoryDto;
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

import java.util.List;


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

        if (productDto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(productDto);
    }


    //{ "name": "Điện thoại iPhone 15 Pro",
    //  "description": "Điện thoại cao cấp với chip A17 Bionic, màn hình 6.1 inch.",
    //  "price": 28990000,
    //  "originalPrice": 32990000,
    //  "discountPercent": 12.1,
    //  "stock": 50,
    //  "imageId": "img_iphone15pro.png",
    //  "status": "AVAILABLE",
    //  "categoryId": "44f7355f-c744-432e-be51-667c89cd2261"}
    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<ProductDto> createProduct(@Valid @RequestPart ProductCreateRequest request,
                                              @RequestPart(required = false) MultipartFile file) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(modelMapper.map(productService.createProduct(request,file), ProductDto.class));
    }

    ////{id:"c844981c-6383-4cc0-9cb9-5510f3751ef2"
    // ,"name": "Điện thoại iPhone 15 Pro",
    //    //  "description": "Điện thoại cao cấp với chip A17 Bionic, màn hình 6.1 inch.",
    //    //  "price": 28990000,
    //    //  "originalPrice": 32990000,
    //    //  "discountPercent": 12.1,
    //    //  "stock": 50,
    //    //  "imageId": "img_iphone15pro.png",
    //    //  "status": "AVAILABLE",
    //    //  "categoryId": "44f7355f-c744-432e-be51-667c89cd2261"}

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

    // {{baseURL}}/v1/stock/product/search?keyword=A
    @GetMapping("/search")
    public ResponseEntity<Page<ProductDto>> searchProducts(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "1") Integer pageNo) {

        Page<Product> products = productService.searchProductByKeyword(keyword, pageNo);
        Page<ProductDto> dtoPage = products.map(product -> modelMapper.map(product, ProductDto.class));
        return ResponseEntity.ok(dtoPage);
    }
    // {{baseURL}}/v1/stock/product/list
//    @GetMapping("/list")
//    ResponseEntity<Page<ProductDto>> getAllProducts(@RequestParam(defaultValue = "1") Integer pageNo) {
//        Page<ProductDto> products = productService.getAllProducts(pageNo).map(product -> modelMapper.map(product, ProductDto.class));
//        return ResponseEntity.status(HttpStatus.OK).body(products);
//    }

    @GetMapping("/list")
    ResponseEntity<List<ProductDto>> getAllProduct(){
        return ResponseEntity.ok(productService.getAllProducts().stream()
                .map(product -> modelMapper.map(product, ProductDto.class)).toList());
    }

}
