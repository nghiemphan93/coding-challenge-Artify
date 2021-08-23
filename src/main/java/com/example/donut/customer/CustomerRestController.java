package com.example.donut.customer;

import com.example.donut.customer.dtos.CustomerCreateDto;
import com.example.donut.customer.dtos.CustomerUpdateDto;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
@AllArgsConstructor
public class CustomerRestController {
    private final CustomerService customerService;

    @GetMapping
    public ResponseEntity<List<Customer>> getAll() {
        return new ResponseEntity<>(this.customerService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getOne(@PathVariable Long id) throws NotFoundException {
        return new ResponseEntity<>(this.customerService.getOne(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Customer> create(@RequestBody @Valid CustomerCreateDto customer) {
        return new ResponseEntity<>(this.customerService.create(customer), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateOne(@PathVariable Long id,
                                              @RequestBody @Valid CustomerUpdateDto customerUpdateDto) throws NotFoundException {
        return new ResponseEntity<>(this.customerService.updateOne(id, customerUpdateDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteOne(@PathVariable Long id) {
        this.customerService.delete(id);
        return new ResponseEntity<HttpStatus>(HttpStatus.ACCEPTED);
    }
}
