package com.example.donut.order;

import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@AllArgsConstructor
public class OrderCustomController {
    private final OrderService orderService;

    @GetMapping("/nextDelivery")
    public ResponseEntity<List<Order>> getNextDelivery() throws NotFoundException {
        return new ResponseEntity<>(this.orderService.getNextDelivery(), HttpStatus.OK);
    }

    @PutMapping("/handleNextDelivery")
    public ResponseEntity<List<Order>> handleNextDelivery() throws NotFoundException {
        return new ResponseEntity<>(this.orderService.handleNextDelivery(), HttpStatus.OK);
    }
}
