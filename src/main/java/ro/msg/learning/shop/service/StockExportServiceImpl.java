package ro.msg.learning.shop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ro.msg.learning.shop.model.Stock;
import ro.msg.learning.shop.repository.LocationRepository;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StockExportServiceImpl implements StockExportService {
    private final LocationRepository locationRepository;

    @Override
    public List<Stock> exportStock(UUID locationId) {
        return locationRepository.findById(locationId)
                .map(location -> location.getStocks().stream().toList())
                .orElse(Collections.emptyList());
    }
}
