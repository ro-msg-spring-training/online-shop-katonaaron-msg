package ro.msg.learning.shop.controller;

import lombok.SneakyThrows;
import org.assertj.core.groups.Tuple;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ro.msg.learning.shop.ControllerIntegrationTest;
import ro.msg.learning.shop.DummyData;
import ro.msg.learning.shop.dto.CreateOrderDTO;
import ro.msg.learning.shop.dto.CreateOrderDetailDTO;
import ro.msg.learning.shop.dto.OrderDTO;
import ro.msg.learning.shop.dto.OrderDetailDTO;
import ro.msg.learning.shop.repository.CustomerRepository;
import ro.msg.learning.shop.repository.LocationRepository;
import ro.msg.learning.shop.repository.ProductCategoryRepository;
import ro.msg.learning.shop.repository.ProductRepository;
import ro.msg.learning.shop.repository.SupplierRepository;

import java.util.List;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class OrderControllerIntegrationTest extends ControllerIntegrationTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private MappingJackson2HttpMessageConverter springMvcJacksonConverter;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private SupplierRepository supplierRepository;
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private ProductCategoryRepository productCategoryRepository;
    @Autowired
    private CustomerRepository customerRepository;

    @SneakyThrows
    private String asJsonString(final Object obj) {
        return springMvcJacksonConverter.getObjectMapper().writeValueAsString(obj);
//        return obj.toString();
    }

    @Before
    public void setUp() {
        customerRepository.save(DummyData.customer);
        supplierRepository.save(DummyData.supplier);
        productCategoryRepository.save(DummyData.category);

        productRepository.saveAll(DummyData.products);

        locationRepository.save(DummyData.locationWithStock20);
    }

    @Test
    public void testCreateOrder_insufficientStock_fail() {
        final var orderDetails = List.of(
                new CreateOrderDetailDTO(DummyData.product1.getId(), 30),
                new CreateOrderDetailDTO(DummyData.product2.getId(), 15)
        );
        testOrderCreationFail(orderDetails, HttpStatus.CONFLICT.value());
    }

    @Test
    public void testCreateOrder_sufficientStock_success() {
        final var orderDetails = List.of(
                new CreateOrderDetailDTO(DummyData.product1.getId(), 10),
                new CreateOrderDetailDTO(DummyData.product2.getId(), 15)
        );

        testOrderCreationSuccess(orderDetails, HttpStatus.CREATED.value());
    }

    @SneakyThrows
    private void testOrderCreationFail(List<CreateOrderDetailDTO> orderDetails, int status) {
        final var addressDto = DummyData.addressDTO;
        final var dto = new CreateOrderDTO(addressDto, orderDetails);

        final var result = mvc.perform(MockMvcRequestBuilders
                        .post("/orders/")
                        .content(asJsonString(dto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().is(status),
                        content().contentType(MediaType.APPLICATION_JSON)
                )
                .andReturn();

        assertThat(result.getResponse().getContentAsString())
                .matches(Pattern.compile("No potential location has the product \\\"[a-z0-9-]+\\\" in stock in the quantity [0-9]+"));
    }

    @SneakyThrows
    private void testOrderCreationSuccess(List<CreateOrderDetailDTO> orderDetails, int status) {
        final var addressDto = DummyData.addressDTO;
        final var dto = new CreateOrderDTO(addressDto, orderDetails);

        final var result = mvc.perform(MockMvcRequestBuilders
                        .post("/orders/")
                        .content(asJsonString(dto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().is(status),
                        content().contentType(MediaType.APPLICATION_JSON)
                )
                .andReturn();

        final OrderDTO orderDto = springMvcJacksonConverter.getObjectMapper().readerFor(OrderDTO.class)
                .readValue(result.getResponse().getContentAsString());

        assertThat(orderDto.deliveryAddress()).isEqualTo(addressDto);
        assertThat(orderDto.orderDetails())
                .hasSize(orderDetails.size())
                .map(od -> od.product().id(), OrderDetailDTO::quantity)
                .containsAll(orderDetails.stream()
                        .map(od -> new Tuple(od.productId(), od.quantity()))
                        .toList());
        assertThat(orderDto.orderDetails())
                .flatExtracting(OrderDetailDTO::shippedFrom)
                .isNotNull();
        assertThat(orderDto.id()).isNotNull();
        assertThat(orderDto.createdAt()).isNotNull();
        assertThat(orderDto.customer()).isNotNull();
    }
}