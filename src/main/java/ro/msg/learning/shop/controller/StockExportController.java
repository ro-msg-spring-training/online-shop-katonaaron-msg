package ro.msg.learning.shop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ro.msg.learning.shop.dto.StockDto;
import ro.msg.learning.shop.mapper.StockMapper;
import ro.msg.learning.shop.service.StockExportService;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class StockExportController {
    private final StockExportService stockExportService;
    private final StockMapper stockMapper;

    @GetMapping(value = "/locations/{id}/exportStock", produces = "text/csv")
    public ResponseEntity<List<StockDto>> exportStock(@PathVariable("id") UUID locationId) {
        return ResponseEntity.ok(
                stockMapper.toDtos(
                        stockExportService.exportStock(locationId)));
    }

}
