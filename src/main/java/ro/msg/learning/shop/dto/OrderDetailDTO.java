package ro.msg.learning.shop.dto;

public record OrderDetailDTO(
        ProductDTO product,
        int quantity
) {
}
