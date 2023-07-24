package ro.msg.learning.shop;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ro.msg.learning.shop.service.locationselection.LocationSelectionAlgorithm;

@Configuration
public class LocationSelectionConfiguration {
    @SneakyThrows
    @Bean
    LocationSelectionAlgorithm locationSelectionAlgorithm(@Value("${ro.msg.learning.shop.location-selection-algorithm}") String className) {
        return Class.forName(className)
                .asSubclass(LocationSelectionAlgorithm.class)
                .getConstructor()
                .newInstance();
    }
}
