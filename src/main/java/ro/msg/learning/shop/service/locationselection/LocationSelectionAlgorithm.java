package ro.msg.learning.shop.service.locationselection;

import ro.msg.learning.shop.model.Address;
import ro.msg.learning.shop.model.OrderDetail;

import java.util.Collection;
import java.util.Set;

public interface LocationSelectionAlgorithm {
    Set<OrderDetail> selectLocationForItems(Address deliveryAddress, Collection<OrderDetailWithPotentialLocations> items);
}
