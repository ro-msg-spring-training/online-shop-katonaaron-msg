package ro.msg.learning.shop.test;

import ro.msg.learning.shop.dto.AddressDTO;
import ro.msg.learning.shop.model.Address;
import ro.msg.learning.shop.model.Customer;
import ro.msg.learning.shop.model.Location;
import ro.msg.learning.shop.model.Product;
import ro.msg.learning.shop.model.ProductCategory;
import ro.msg.learning.shop.model.Stock;
import ro.msg.learning.shop.model.Supplier;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.Collections.emptySet;

public final class DummyData {
    public static final Address addressMSGBrassai = new Address("Romania", "Cluj-Napoca", "Cluj", "Str. Brassai 9");
    public static final AddressDTO addressDTOMSGBrassai = new AddressDTO("Romania", "Cluj-Napoca", "Cluj", "Str. Brassai 9");
    public static final Product theJungleBook;
    public static final Product coloredPencils;
    public static final List<Product> products;
    public static final Location locationWithEmptyStock;
    public static final String LOCATION_WITH_STOCK_20_ID = "a36d5e38-2bb5-11ee-be56-0242ac120002";
    public static final Location locationWithStock20;
    public static final String LOCATION_WITH_STOCK_40_ID = "b42da723-3501-4e23-82ff-763660133c6c";
    public static final Location locationWithStock40;
    public static final List<Location> locations;
    public static final Supplier amazon = new Supplier("Amazon");
    public static final Supplier faberCastell = new Supplier("Faber-Castell");
    public static final List<Supplier> suppliers = List.of(amazon, faberCastell);
    public static final ProductCategory bookCategory = new ProductCategory("Book", "Category of books");
    public static final ProductCategory pencilCategory = new ProductCategory("Pencil", "Category of pencils");
    public static final List<ProductCategory> productCategories = List.of(bookCategory, pencilCategory);
    public static final Customer johnSmith = new Customer(
            "John", "Smith", "johnsmith",
            "P4ssw0rd*90!", "johnsmith@gmail.com");
    public static final List<Customer> customers = List.of(johnSmith);


    static {
        theJungleBook = new Product(
                "The Jungle Book",
                "The Jungle Book is a collection of stories by the English author Rudyard Kipling.",
                BigDecimal.valueOf(12.5),
                0.2,
                bookCategory,
                amazon,
                "https://th.bing.com/th?id=OSK.899ef36ef6c3971cf2b9a4bff665159e&w=124&h=168&c=7&o=6&dpr=1.3&pid=SANGAM"
        );
        coloredPencils = new Product(
                "Colored Pencils",
                "Faber-Castell Goldfaber colored pencils",
                BigDecimal.valueOf(208),
                0.2,
                pencilCategory,
                faberCastell,
                "https://www.fabercastell.com/cdn/shop/products/vwa0m0vmeiimg0awde2o_720x.jpg?v=1684499795"
        );
        products = List.of(theJungleBook, coloredPencils);

        locationWithEmptyStock = new Location(
                "Dezmir Warehouse",
                new Address("Romania", "Dezmir", "Cluj", "str. Fabricii 2"),
                emptySet()
        );
        locationWithStock20 = new Location(
                "Cluj-Napoca Warehouse",
                new Address("Romania", "Cluj-Napoca", "Cluj", "str. Fabricii 2"),
                createStocks(20, products)
        );
        locationWithStock20.setId(UUID.fromString(LOCATION_WITH_STOCK_20_ID));
        locationWithStock40 = new Location(
                "Bucuresti Warehouse",
                new Address("Romania", "Bucuresti", "Bucuresti", "str. Fabricii 2"),
                createStocks(40, products)
        );
        locationWithStock40.setId(UUID.fromString(LOCATION_WITH_STOCK_40_ID));
        locations = List.of(locationWithEmptyStock, locationWithStock20, locationWithStock40);
    }

    private DummyData() {

    }

    public static Set<Stock> createStocks(int productQuantity, Product... products) {
        return createStocks(productQuantity, Arrays.stream(products).toList());
    }

    public static Set<Stock> createStocks(int productQuantity, List<Product> products) {
        return products.stream()
                .map(p -> new Stock(p, productQuantity))
                .collect(Collectors.toSet());
    }

    public static Set<Stock> createStocks(Map<Product, Integer> productToQuantity) {
        return productToQuantity.entrySet().stream()
                .map(entry -> new Stock(entry.getKey(), entry.getValue()))
                .collect(Collectors.toSet());
    }
}
