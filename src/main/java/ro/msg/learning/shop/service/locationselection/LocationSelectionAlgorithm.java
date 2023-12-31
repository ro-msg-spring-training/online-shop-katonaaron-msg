package ro.msg.learning.shop.service.locationselection;

import ro.msg.learning.shop.model.OrderDetail;

import java.util.Set;

public interface LocationSelectionAlgorithm {
    Set<OrderDetail> selectLocationForItems(OrderWithPotentialLocations order);
}
