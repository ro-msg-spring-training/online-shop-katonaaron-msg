package ro.msg.learning.shop.mapper;

import org.mapstruct.Mapper;
import ro.msg.learning.shop.dto.OrderDetailDTO;
import ro.msg.learning.shop.model.OrderDetail;

@Mapper(uses = {ProductMapper.class, LocationMapper.class})
public interface OrderDetailsMapper {

    OrderDetailDTO toDto(OrderDetail orderDetail);
}
