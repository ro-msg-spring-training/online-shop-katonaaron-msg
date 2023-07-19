package ro.msg.learning.shop.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ro.msg.learning.shop.dto.OrderDTO;
import ro.msg.learning.shop.model.Order;

import java.util.Collection;
import java.util.List;

@Mapper(uses = {CustomerMapper.class, AddressMapper.class, OrderDetailsMapper.class})
public interface OrderMapper {
    @Mapping(target = "shippedFrom", expression = "java(order.getShippedFrom().getName())")
    OrderDTO toDto(Order order);

    List<OrderDTO> toDtos(Collection<Order> order);
}
