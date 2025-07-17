package com.example.stockservice.service.product;

import com.example.stockservice.client.FileStorageClient;
import com.example.stockservice.dto.ProductDto;
import com.example.stockservice.emuns.ProductStatus;
import com.example.stockservice.model.Product;
import com.example.stockservice.repository.ProductRepository;
import com.example.stockservice.request.product.ProductCreateRequest;
import com.example.stockservice.request.product.ProductUpdateRequest;
import com.example.stockservice.service.category.CategoryService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final CategoryService categoryService;
    private final ProductRepository productRepository;
    private final FileStorageClient fileStorageClient;
    private final ModelMapper modelMapper;

    @Override
    public void decreaseStock(String productId, int quantity) {
        Product product = getProductById(productId);
        if (product.getStock() < quantity) {
            throw new RuntimeException("Insufficient stock for product: " + productId);
        }
        product.setStock(product.getStock() - quantity);
        productRepository.save(product);
    }

    @Override
    public Product createProduct(ProductCreateRequest request, MultipartFile multipartFile) {
        String imageId = request.getImageId();
        if (multipartFile != null) {
            imageId = fileStorageClient.uploadImageToFIleSystem(multipartFile).getBody();
        }
        return productRepository.save(
                Product.builder()
                        .name(request.getName())
                        .description(request.getDescription())
                        .price(request.getPrice())
                        .originalPrice(request.getOriginalPrice())
                        .discountPercent(request.getDiscountPercent())
                        .stock(request.getStock())
                        .status(ProductStatus.IN_STOCK)
                        .category(categoryService.findCategoryById(request.getCategoryId()))
                        .imageId(imageId)
                        .build()
        );
    }

    @Override
    public Product updateProduct(ProductUpdateRequest request, MultipartFile multipartFile) {
       Product toUpdate = findProductById(request.getId());
        modelMapper.map(request, toUpdate);
        if (multipartFile != null) {
            String imageId = fileStorageClient.uploadImageToFIleSystem(multipartFile).getBody();
            if (imageId != null) {
                fileStorageClient.deleteImageFromFileSystem(toUpdate.getImageId());
                toUpdate.setImageId(imageId);
            }
        }
        return productRepository.save(toUpdate);
    }

    @Override
    public Product getProductById(String id) {
        return findProductById(id);
    }

    @Override
    public Product findProductById(String id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }

    @Override
    public void deleteProduct(String id) {
        productRepository.deleteById(id);
    }

    @Override
    public Page<Product> getAllProducts(Integer pageNo) {
        Pageable pageable = PageRequest.of(pageNo - 1, 10);
        return productRepository.findAll(pageable);
    }

    protected Page<Product> fetchPageFromDB(String keyword, Integer pageNo) {
       List<Product> fullList = productRepository.searchProductByName(keyword);
       Pageable pageable = PageRequest.of(pageNo -1, 5);
       int start = Math.min((int) pageable.getOffset(), fullList.size());
       int end = Math.min(start + pageable.getPageSize(), fullList.size());
       List<Product> pageList = fullList.subList(start, end);
       return  new PageImpl<>(pageList, pageable, fullList.size());
    }

    @Override
    public Page<Product> searchProductByKeyword(String keyword, Integer pageNo) {
        return fetchPageFromDB(keyword, pageNo);
    }


    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
}
