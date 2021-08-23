package com.example.donut.customer;

import com.example.donut.order.Order;
import com.example.donut.order.OrderService;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/customers")
@AllArgsConstructor
public class CustomerCustomController {
    private final OrderService orderService;

    @GetMapping("/{id}/currentOrder")
    public ResponseEntity<Order> getCurrentOrder(@PathVariable Long id) throws NotFoundException {
        return new ResponseEntity<>(this.orderService.getCurrentOrder(id), HttpStatus.OK);
    }

    @PutMapping("/{id}/cancelOrder")
    public ResponseEntity<Order> cancelOrder(@PathVariable Long id) throws NotFoundException {
        return new ResponseEntity<>(this.orderService.cancelOrder(id), HttpStatus.OK);
    }
}
