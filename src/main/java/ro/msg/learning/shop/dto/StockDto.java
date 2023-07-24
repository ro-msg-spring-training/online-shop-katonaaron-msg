package ro.msg.learning.shop.dto;

import java.util.UUID;

public record StockDto(
        UUID productId,
        String productName,
        UUID locationId,
        String locationName,
        int quantity
) {
}
