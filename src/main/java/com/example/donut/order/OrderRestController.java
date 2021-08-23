package com.example.donut.order;

import com.example.donut.order.dtos.OrderCreateDto;
import com.example.donut.order.dtos.OrderUpdateDto;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@AllArgsConstructor
public class OrderRestController {
    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<List<Order>> getAll() {
        return new ResponseEntity<>(this.orderService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOne(@PathVariable Long id) throws NotFoundException {
        return new ResponseEntity<>(this.orderService.getOne(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Order> create(@RequestBody @Valid OrderCreateDto createDto) throws NotFoundException {
        return new ResponseEntity<>(this.orderService.create(createDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOne(@PathVariable Long id,
                                           @RequestBody @Valid OrderUpdateDto updateDto) throws NotFoundException {
        return new ResponseEntity<>(this.orderService.updateOne(id, updateDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteOne(@PathVariable Long id) {
        this.orderService.delete(id);
        return new ResponseEntity<HttpStatus>(HttpStatus.ACCEPTED);
    }
}
