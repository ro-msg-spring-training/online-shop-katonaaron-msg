package ro.msg.learning.shop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ro.msg.learning.shop.model.Product;
import ro.msg.learning.shop.repository.ProductRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public Product createProduct(Product product) {
        if (product.getId() == null) {
            product.setId(UUID.randomUUID());
        }
        return productRepository.saveAndFlush(product);
    }

    public Product replaceProduct(UUID id, Product product) {
        product.setId(id);
        return productRepository.saveAndFlush(product);
    }

    public void deleteProduct(Product product) {
        productRepository.delete(product);
    }

    public void deleteProductById(UUID productId) {
        findProductById(productId).ifPresent(this::deleteProduct);
    }

    public Optional<Product> findProductById(UUID productId) {
        return productRepository.findById(productId);
    }

    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }
}
