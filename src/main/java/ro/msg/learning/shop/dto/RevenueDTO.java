package ro.msg.learning.shop.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record RevenueDTO(
        LocationDTOOverview location,
        LocalDate date,
        BigDecimal sum
) {
}
