package ro.msg.learning.shop.controller;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.msg.learning.shop.dto.CreateProductDto;
import ro.msg.learning.shop.dto.ProductDTO;
import ro.msg.learning.shop.mapper.ProductMapper;
import ro.msg.learning.shop.service.ProductService;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;
    private final ProductMapper productMapper;

    @GetMapping("/")
    public ResponseEntity<List<ProductDTO>> findAllProducts() {
        return ResponseEntity.ok(
                productMapper.toDtos(
                        productService.findAllProducts()));
    }

    @PostMapping("/")
    public ResponseEntity<ProductDTO> createProduct(@RequestBody CreateProductDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                productMapper.toDto(
                        productService.createProduct(
                                productMapper.toEntity(dto),
                                dto.categoryId(),
                                dto.supplierId()
                        )
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> findProductById(@PathVariable @NotNull UUID id) {
        return productService.findProductById(id)
                .map(product -> ResponseEntity.ok(
                        productMapper.toDto(product))
                ).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> replaceProduct(@PathVariable @NotNull UUID id, @RequestBody CreateProductDto dto) {
        return ResponseEntity.ok(
                productMapper.toDto(
                        productService.replaceProduct(
                                id,
                                productMapper.toEntity(dto),
                                dto.categoryId(),
                                dto.supplierId()
                        )
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ProductDTO> replaceProduct(@PathVariable @NotNull UUID id) {
        productService.deleteProductById(id);
        return ResponseEntity.noContent().build();
    }
}
