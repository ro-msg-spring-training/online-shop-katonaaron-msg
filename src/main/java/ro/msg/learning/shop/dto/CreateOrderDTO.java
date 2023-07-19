package ro.msg.learning.shop.dto;

import java.time.LocalDateTime;
import java.util.List;

public record CreateOrderDTO(
        LocalDateTime timestamp,
        AddressDTO deliveryAddress,
        List<CreateOrderDetailDTO> orderDetails
) {
}
