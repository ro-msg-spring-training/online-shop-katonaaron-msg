package ro.msg.learning.shop.controller;

import lombok.SneakyThrows;
import org.assertj.core.groups.Tuple;
import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ro.msg.learning.shop.IntegrationTest;
import ro.msg.learning.shop.dto.CreateOrderDTO;
import ro.msg.learning.shop.dto.CreateOrderDetailDTO;
import ro.msg.learning.shop.dto.OrderDTO;
import ro.msg.learning.shop.dto.OrderDetailDTO;
import ro.msg.learning.shop.model.Customer;
import ro.msg.learning.shop.test.DummyData;

import java.util.Base64;
import java.util.List;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ro.msg.learning.shop.test.DummyData.coloredPencils;
import static ro.msg.learning.shop.test.DummyData.johnSmith;
import static ro.msg.learning.shop.test.DummyData.theJungleBook;

public class OrderControllerIntegrationTest extends IntegrationTest {
    @NotNull
    private static String getAuthorizationHeader(Customer customer) {
        return "Basic " + Base64.getEncoder().encodeToString((customer.getUsername() + ":" + customer.getPassword()).getBytes());
    }

    @Before
    @SneakyThrows
    public void setUp() {
        mvc.perform(MockMvcRequestBuilders
                        .post("/test/clearAndPopulate"))
                .andReturn();
    }

    @Test
    public void testCreateOrder_insufficientStock_fail() {
        final var orderDetails = List.of(
                new CreateOrderDetailDTO(theJungleBook.getId(), 100),
                new CreateOrderDetailDTO(coloredPencils.getId(), 15)
        );
        testOrderCreationFail(orderDetails, HttpStatus.CONFLICT.value());
    }

    @Test
    public void testCreateOrder_sufficientStock_success() {
        final var orderDetails = List.of(
                new CreateOrderDetailDTO(theJungleBook.getId(), 10),
                new CreateOrderDetailDTO(coloredPencils.getId(), 15)
        );

        testOrderCreationSuccess(orderDetails, HttpStatus.CREATED.value());
    }

    @SneakyThrows
    private void testOrderCreationFail(List<CreateOrderDetailDTO> orderDetails, int status) {
        final var addressDto = DummyData.addressDTOMSGBrassai;
        final var dto = new CreateOrderDTO(addressDto, orderDetails);

        final var result = mvc.perform(MockMvcRequestBuilders
                        .post("/orders/")
                        .header(HttpHeaders.AUTHORIZATION, getAuthorizationHeader(johnSmith))
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
        final var addressDto = DummyData.addressDTOMSGBrassai;
        final var dto = new CreateOrderDTO(addressDto, orderDetails);

        final var result = mvc.perform(MockMvcRequestBuilders
                        .post("/orders/")
                        .header(HttpHeaders.AUTHORIZATION, getAuthorizationHeader(johnSmith))
                        .content(asJsonString(dto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().is(status),
                        content().contentType(MediaType.APPLICATION_JSON)
                )
                .andReturn();

        final OrderDTO orderDto = objectMapper.readerFor(OrderDTO.class)
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