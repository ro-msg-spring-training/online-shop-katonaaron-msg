package ro.msg.learning.shop.service;

import ro.msg.learning.shop.model.Product;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductService {
    Product createProduct(Product product, UUID productCategoryId, UUID supplierId);

    Product replaceProduct(UUID id, Product product, UUID productCategoryId, UUID supplierId);

    void deleteProduct(Product product);

    void deleteProductById(UUID productId);

    Optional<Product> findProductById(UUID productId);

    List<Product> findAllProducts();
}
