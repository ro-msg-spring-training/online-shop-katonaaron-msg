package ro.msg.learning.shop.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import ro.msg.learning.shop.dto.CreateProductDto;
import ro.msg.learning.shop.dto.ProductDTO;
import ro.msg.learning.shop.exception.ResourceNotFoundException;
import ro.msg.learning.shop.model.Product;
import ro.msg.learning.shop.model.ProductCategory;
import ro.msg.learning.shop.model.Supplier;
import ro.msg.learning.shop.repository.ProductCategoryRepository;
import ro.msg.learning.shop.repository.SupplierRepository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring")
public abstract class ProductMapper {

    @Autowired
    protected ProductCategoryRepository categoryRepository;
    @Autowired
    protected SupplierRepository supplierRepository;


    @Mapping(target = "categoryName", expression = "java(product.getCategory().getName())")
    @Mapping(target = "categoryDescription", expression = "java(product.getCategory().getDescription())")
    @Mapping(target = "supplier", expression = "java(product.getSupplier().getName())")
    public abstract ProductDTO toDto(Product product);

    public abstract List<ProductDTO> toDtos(Collection<Product> product);

    @Mapping(target = "category", expression = "java(findCategoryById(createProductDto.categoryId()))")
    @Mapping(target = "supplier", expression = "java(findSupplierById(createProductDto.supplierId()))")
    public abstract Product toEntity(CreateProductDto createProductDto);

    protected ProductCategory findCategoryById(UUID categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("product category with id \"" + categoryId + "\" was not found"));
    }

    protected Supplier findSupplierById(UUID supplierId) {
        return supplierRepository.findById(supplierId).orElseThrow(() -> new ResourceNotFoundException("supplier with id \"" + supplierId + "\" was not found"));
    }
}
