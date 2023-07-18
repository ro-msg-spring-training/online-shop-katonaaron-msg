package ro.msg.learning.shop.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateProductDto(
        String name,
        String description,
        BigDecimal price,
        Double weight,
        UUID categoryId,
        UUID supplierId,
        String imageUrl) {
}
