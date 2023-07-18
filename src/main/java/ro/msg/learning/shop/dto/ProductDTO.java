package ro.msg.learning.shop.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductDTO(
        UUID id,
        String name,
        String description,
        BigDecimal price,
        Double weight,
        String categoryName,
        String categoryDescription,
        String supplier,
        String imageUrl) {
}
