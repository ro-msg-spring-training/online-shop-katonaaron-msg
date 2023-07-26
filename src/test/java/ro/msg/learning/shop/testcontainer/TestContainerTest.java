package ro.msg.learning.shop.testcontainer;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ro.msg.learning.shop.IntegrationTest;
import ro.msg.learning.shop.model.Address;
import ro.msg.learning.shop.model.Location;
import ro.msg.learning.shop.repository.LocationRepository;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;


public class TestContainerTest extends IntegrationTest {

    @Autowired
    LocationRepository locationRepository;

    private Location location;

    @Before
    public void setUp() {
        location = new Location();
        location.setName("TestLocation");
        var address = new Address();
        address.setCity("city");
        address.setStreetAddress("streetAddress");
        address.setCounty("county");
        address.setCountry("country");
        location.setAddress(address);
        location.setStocks(Collections.emptySet());
    }

    @Test
    public void testTC_creation_works() {
        test();
    }

    @Test
    public void testTC_recreation_works() {
        test();
    }

    private void test() {
        assertThat(locationRepository.count()).isZero();
        locationRepository.save(location);
        final var locations = locationRepository.findAll();
        assertThat(locations).singleElement().isEqualTo(location);
    }


}