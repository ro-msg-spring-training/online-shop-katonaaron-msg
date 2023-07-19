package ro.msg.learning.shop.dto;

import java.util.UUID;

public record CreateOrderDetailDTO(
        UUID productId,
        int quantity
) {
}
