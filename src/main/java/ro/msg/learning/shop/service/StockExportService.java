package ro.msg.learning.shop.service;

import ro.msg.learning.shop.model.Stock;

import java.util.List;
import java.util.UUID;

public interface StockExportService {
    List<Stock> exportStock(UUID locationId);
}
