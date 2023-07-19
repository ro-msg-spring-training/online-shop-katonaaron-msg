package ro.msg.learning.shop.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ro.msg.learning.shop.dto.CreateProductDto;
import ro.msg.learning.shop.dto.ProductDTO;
import ro.msg.learning.shop.model.Product;

import java.util.Collection;
import java.util.List;

@Mapper
public interface ProductMapper {

    @Mapping(target = "categoryName", expression = "java(product.getCategory().getName())")
    @Mapping(target = "categoryDescription", expression = "java(product.getCategory().getDescription())")
    @Mapping(target = "supplier", expression = "java(product.getSupplier().getName())")
    ProductDTO toDto(Product product);

    List<ProductDTO> toDtos(Collection<Product> product);

    @Mapping(target = "category", ignore = true)
    @Mapping(target = "supplier", ignore = true)
    Product toEntity(CreateProductDto createProductDto);
}
