package ro.msg.learning.shop.mapper;

import org.mapstruct.Mapper;
import ro.msg.learning.shop.dto.CustomerDto;
import ro.msg.learning.shop.model.Customer;

@Mapper
public interface CustomerMapper {

    CustomerDto toDto(Customer customer);
}
