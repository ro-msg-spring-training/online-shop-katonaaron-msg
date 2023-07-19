package ro.msg.learning.shop.mapper;

import org.mapstruct.Mapper;
import ro.msg.learning.shop.dto.AddressDTO;
import ro.msg.learning.shop.model.Address;

@Mapper
public interface AddressMapper {
    AddressDTO toDto(Address address);

    Address toEntity(AddressDTO addressDTO);
}
