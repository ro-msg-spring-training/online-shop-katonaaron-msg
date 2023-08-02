package ro.msg.learning.shop.mapper;

import org.mapstruct.Mapper;
import ro.msg.learning.shop.dto.RevenueDTO;
import ro.msg.learning.shop.model.Revenue;

@Mapper(uses = LocationMapper.class)
public interface RevenueMapper {

    RevenueDTO toDto(Revenue revenue);
}
