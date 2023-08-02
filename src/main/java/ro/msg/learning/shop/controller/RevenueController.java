package ro.msg.learning.shop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ro.msg.learning.shop.dto.RevenueDTO;
import ro.msg.learning.shop.mapper.RevenueMapper;
import ro.msg.learning.shop.service.RevenueService;

import java.time.LocalDate;
import java.util.Collection;

@RestController
@RequiredArgsConstructor
public class RevenueController {
    private final RevenueService revenueService;
    private final RevenueMapper revenueMapper;

    @GetMapping("/revenues")
    public ResponseEntity<Collection<RevenueDTO>> getRevenueOfDay(@RequestParam LocalDate day) {
        return ResponseEntity.ok(
                revenueService.getRevenueForDay(day).stream()
                        .map(revenueMapper::toDto)
                        .toList());
    }
}
