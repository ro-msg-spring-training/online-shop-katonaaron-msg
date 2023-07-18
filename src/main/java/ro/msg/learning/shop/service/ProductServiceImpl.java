package ro.msg.learning.shop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ro.msg.learning.shop.exception.ResourceNotFoundException;
import ro.msg.learning.shop.model.Product;
import ro.msg.learning.shop.model.ProductCategory;
import ro.msg.learning.shop.model.Supplier;
import ro.msg.learning.shop.repository.ProductCategoryRepository;
import ro.msg.learning.shop.repository.ProductRepository;
import ro.msg.learning.shop.repository.SupplierRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final SupplierRepository supplierRepository;

    @Override
    public Product createProduct(Product product, UUID productCategoryId, UUID supplierId) {
        if (product.getId() == null) {
            product.setId(UUID.randomUUID());
        }
        product.setCategory(findProductCategoryById(productCategoryId));
        product.setSupplier(findSupplierById(supplierId));
        return productRepository.saveAndFlush(product);
    }

    private Supplier findSupplierById(UUID supplierId) {
        return supplierRepository.findById(supplierId)
                .orElseThrow(() -> new ResourceNotFoundException("supplier with id \"" + supplierId + "\" was not found"));
    }

    private ProductCategory findProductCategoryById(UUID productCategoryId) {
        return productCategoryRepository.findById(productCategoryId)
                .orElseThrow(() -> new ResourceNotFoundException("product category with id \"" + productCategoryId + "\" was not found"));
    }

    @Override
    public Product replaceProduct(UUID id, Product product, UUID productCategoryId, UUID supplierId) {
        product.setId(id);
        product.setCategory(findProductCategoryById(productCategoryId));
        product.setSupplier(findSupplierById(supplierId));
        return productRepository.saveAndFlush(product);
    }

    @Override
    public void deleteProduct(Product product) {
        productRepository.delete(product);
    }

    @Override
    public void deleteProductById(UUID productId) {
        findProductById(productId).ifPresent(this::deleteProduct);
    }

    @Override
    public Optional<Product> findProductById(UUID productId) {
        return productRepository.findById(productId);
    }

    @Override
    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }
}
