package ro.msg.learning.shop.mapper;

import org.mapstruct.Mapper;
import ro.msg.learning.shop.dto.LocationDTOOverview;
import ro.msg.learning.shop.model.Location;

@Mapper(uses = AddressMapper.class)
public interface LocationMapper {

    LocationDTOOverview toOverviewDto(Location location);
}
