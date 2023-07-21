package ro.msg.learning.shop.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record OrderDTO(
        UUID id,
        CustomerDto customer,
        LocalDateTime createdAt,
        AddressDTO deliveryAddress,
        List<OrderDetailDTO> orderDetails
) {
}
