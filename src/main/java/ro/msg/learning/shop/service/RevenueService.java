package ro.msg.learning.shop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ro.msg.learning.shop.model.Location;
import ro.msg.learning.shop.model.Revenue;
import ro.msg.learning.shop.repository.LocationRepository;
import ro.msg.learning.shop.repository.OrderDetailRepository;
import ro.msg.learning.shop.repository.RevenueRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RevenueService {
    private final OrderDetailRepository orderDetailRepository;
    private final LocationRepository locationRepository;
    private final RevenueRepository revenueRepository;

    public List<Revenue> getRevenueForDay(LocalDate date) {
        return revenueRepository.findByDate(date);
    }

    @Scheduled(cron = "0 1 0 * * ?", zone = "Europe/Bucharest")
    public void saveRevenueOfYesterday() {
        final var date = LocalDate.now().minusDays(1);
        locationRepository.findAll().stream()
                .map(location -> new Revenue(
                        location,
                        date,
                        findLocationRevenueForDay(location, date)
                ))
                .forEach(revenueRepository::save);
    }

    private BigDecimal findLocationRevenueForDay(Location location, LocalDate date) {
        return orderDetailRepository.findByOrder_CreatedAtBetweenAndShippedFrom_Id(
                        date.atStartOfDay(),
                        date.atTime(LocalTime.MAX),
                        location.getId()).stream()
                .map(od -> od.getProduct().getPrice().multiply(BigDecimal.valueOf(od.getQuantity())))
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }
}
