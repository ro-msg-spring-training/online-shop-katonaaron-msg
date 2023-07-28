package ro.msg.learning.shop.dto;

import java.util.List;

public record CreateOrderDTO(
        AddressDTO deliveryAddress,
        List<CreateOrderDetailDTO> orderDetails
) {
}
