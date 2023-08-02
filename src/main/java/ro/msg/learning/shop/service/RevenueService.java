package ro.msg.learning.shop.service;

import org.springframework.scheduling.annotation.Scheduled;
import ro.msg.learning.shop.model.Revenue;

import java.time.LocalDate;
import java.util.List;

public interface RevenueService {
    List<Revenue> getRevenueForDay(LocalDate date);

    @Scheduled(cron = "0 1 0 * * ?", zone = "Europe/Bucharest")
    void saveRevenueOfYesterday();
}
