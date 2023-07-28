package ro.msg.learning.shop.model;

import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
public class Address {
    private String country;
    private String city;
    private String county;
    private String streetAddress;

    public Address(String country, String city, String county, String streetAddress) {
        this.country = country;
        this.city = city;
        this.county = county;
        this.streetAddress = streetAddress;
    }
}
