package ro.msg.learning.shop;

import ro.msg.learning.shop.dto.AddressDTO;
import ro.msg.learning.shop.model.Address;
import ro.msg.learning.shop.model.Customer;
import ro.msg.learning.shop.model.Location;
import ro.msg.learning.shop.model.Product;
import ro.msg.learning.shop.model.ProductCategory;
import ro.msg.learning.shop.model.Stock;
import ro.msg.learning.shop.model.Supplier;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public final class DummyData {
    public static final Address address;
    public static final AddressDTO addressDTO;
    public static final Product product1;
    public static final Product product2;
    public static final List<Product> products;
    public static final Location locationWithEmptyStock;
    public static final String LOCATION_WITH_STOCK_20_ID = "a36d5e38-2bb5-11ee-be56-0242ac120002";
    public static final Location locationWithStock20;
    public static final String LOCATION_WITH_STOCK_40_ID = "b42da723-3501-4e23-82ff-763660133c6c";
    public static final Location locationWithStock40;
    public static final Supplier supplier;
    public static final ProductCategory category;
    public static final Customer customer;

    static {
        customer = new Customer();
        customer.setUsername("customer");
        customer.setFirstName("customer");
        customer.setLastName("customer");
        customer.setEmailAddress("customer@customer.com");
        customer.setPassword("customerpassword");

        supplier = new Supplier();
        supplier.setName("TestSupplier");

        category = new ProductCategory();
        category.setName("TestCategory");
        category.setDescription("TestCategoryDescription");

        address = createAddress(1);
        addressDTO = new AddressDTO("country", "city", "county", "streetAddress");

        product1 = DummyData.createProduct(1);
        product2 = DummyData.createProduct(2);
        products = List.of(product1, product2);

        locationWithEmptyStock = new Location();
        locationWithEmptyStock.setName("Location-with-empty-stock");
        locationWithEmptyStock.setAddress(address);
        locationWithEmptyStock.setStocks(Collections.emptySet());
        locationWithStock20 = createLocation(20);
        locationWithStock20.setId(UUID.fromString(LOCATION_WITH_STOCK_20_ID));
        locationWithStock40 = createLocation(40);
        locationWithStock40.setId(UUID.fromString(LOCATION_WITH_STOCK_40_ID));
    }

    private DummyData() {

    }

    public static Address createAddress(int no) {
        var address = new Address();
        address.setCity("city" + no);
        address.setStreetAddress("streetAddress" + no);
        address.setCounty("county" + no);
        address.setCountry("country" + no);
        return address;
    }

    public static Product createProduct(int no) {
        var product = new Product();
        product.setName("Product" + no);
        product.setPrice(BigDecimal.valueOf(no));
        product.setWeight((double) no);
        product.setImageUrl("imageUrl" + no);
        product.setSupplier(supplier);
        product.setCategory(category);
        return product;
    }

    public static Location createLocation(int productQuantity) {
        return createLocation(productQuantity, productQuantity);
    }

    public static Location createLocation(int no, int productQuantity) {
        return createLocation(no, productQuantity, products);
    }

    public static Location createLocation(int no, int productQuantity, Collection<Product> products) {
        final var stocks = products.stream()
                .map(p -> new Stock(p, productQuantity))
                .collect(Collectors.toSet());
        return createLocation(no, stocks);
    }

    public static Location createLocation(int no, Collection<Stock> stocks) {
        Location location = new Location();
        location.setName("Location-" + no);
        location.setAddress(createAddress(no));
        location.setStocks(new HashSet<>(stocks));
        return location;
    }
}
