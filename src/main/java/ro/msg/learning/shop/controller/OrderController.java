package ro.msg.learning.shop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.msg.learning.shop.dto.CreateOrderDTO;
import ro.msg.learning.shop.dto.OrderDTO;
import ro.msg.learning.shop.mapper.OrderMapper;
import ro.msg.learning.shop.service.OrderService;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final OrderMapper orderMapper;

    @GetMapping("/")
    public ResponseEntity<List<OrderDTO>> findAllOrders() {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                orderMapper.toDtos(
                        orderService.findAllOrders()
                )
        );
    }

    @PostMapping("/")
    public ResponseEntity<OrderDTO> createOrder(@RequestBody CreateOrderDTO createOrderDTO) {
        return ResponseEntity.ok(
                orderMapper.toDto(
                        orderService.createOrder(createOrderDTO)
                )
        );
    }

}
