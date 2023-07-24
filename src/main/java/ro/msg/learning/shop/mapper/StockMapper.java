package ro.msg.learning.shop.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ro.msg.learning.shop.dto.StockDto;
import ro.msg.learning.shop.model.Stock;

import java.util.Collection;
import java.util.List;

@Mapper
public interface StockMapper {

    @Mapping(target = "productId", expression = "java(stock.getProduct().getId())")
    @Mapping(target = "productName", expression = "java(stock.getProduct().getName())")
    @Mapping(target = "locationId", expression = "java(stock.getLocation().getId())")
    @Mapping(target = "locationName", expression = "java(stock.getLocation().getName())")
    StockDto toDto(Stock stock);

    List<StockDto> toDtos(Collection<Stock> stock);
}
