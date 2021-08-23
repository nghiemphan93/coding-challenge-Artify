package com.example.donut.customer;

import com.example.donut.customer.dtos.CustomerCreateDto;
import com.example.donut.customer.dtos.CustomerDto;
import com.example.donut.customer.dtos.CustomerUpdateDto;
import com.example.donut.order.Order;
import com.example.donut.order.OrderService;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CustomerService {
    private final CustomerRepo customerRepo;

    private Customer customerDto2customer(CustomerDto customerDto, Customer customer) {
        customer.setId(customerDto.getId());
        customer.setPremium(isPremium(customer.getId()));
        customer.setCreatedAt(new Date());
        if (customerDto instanceof CustomerUpdateDto) {
            customer.setCreatedAt(((CustomerUpdateDto) customerDto).getCreatedAt());
        }
        return customer;
    }

    public boolean isPremium(Long id) {
        return id < 1000L;
    }

    public List<Customer> getAll() {
        return (List<Customer>) this.customerRepo.findAll();
    }

    public Customer getOne(Long id) throws NotFoundException {
        return this.customerRepo.findById(id)
                .orElseThrow(() -> new NotFoundException(Customer.class.getSimpleName() + " not found"));
    }

    public Customer create(CustomerCreateDto customerCreateDto) {
        Customer customer = this.customerDto2customer(customerCreateDto, new Customer());
        return this.customerRepo.save(customer);
    }

    public Customer updateOne(Long id, CustomerUpdateDto customerUpdateDto) throws NotFoundException {
        Customer oldEntity = this.getOne(id);
        Customer newEntity = this.customerDto2customer(customerUpdateDto, oldEntity);
        return this.customerRepo.save(newEntity);
    }

    public void delete(Long id) {
        this.customerRepo.deleteById(id);
    }


}
