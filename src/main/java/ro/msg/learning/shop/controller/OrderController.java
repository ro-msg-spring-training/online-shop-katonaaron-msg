package ro.msg.learning.shop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.msg.learning.shop.dto.CreateOrderDTO;
import ro.msg.learning.shop.dto.CreateOrderDetailDTO;
import ro.msg.learning.shop.dto.OrderDTO;
import ro.msg.learning.shop.mapper.OrderMapper;
import ro.msg.learning.shop.service.OrderService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final OrderMapper orderMapper;

    @GetMapping("/")
    public ResponseEntity<List<OrderDTO>> findAllOrders() {
        return ResponseEntity.ok(
                orderMapper.toDtos(
                        orderService.findAllOrders()
                )
        );
    }

    @PostMapping("/")
    public ResponseEntity<OrderDTO> createOrder(@RequestBody CreateOrderDTO createOrderDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                orderMapper.toDto(
                        orderService.createOrder(LocalDateTime.now(),
                                createOrderDTO.deliveryAddress(),
                                createOrderDTO.orderDetails().stream()
                                        .collect(Collectors.toMap(CreateOrderDetailDTO::productId, CreateOrderDetailDTO::quantity)))
                )
        );
    }

}
