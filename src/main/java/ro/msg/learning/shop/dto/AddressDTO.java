package ro.msg.learning.shop.dto;

public record AddressDTO(
        String country,
        String city,
        String county,
        String streetAddress
) {
}
