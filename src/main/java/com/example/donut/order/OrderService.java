package com.example.donut.order;

import com.example.donut.config.RandomDate;
import com.example.donut.customer.Customer;
import com.example.donut.customer.CustomerService;
import com.example.donut.order.dtos.OrderCreateDto;
import com.example.donut.order.dtos.OrderUpdateDto;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrderService {
    private final OrderRepo orderRepo;
    private final CustomerService customerService;
    private final int CART_MAX_SIZE = 50;

    private Order orderCreateDto2order(OrderCreateDto orderCreateDto, Order order) throws NotFoundException {
        order.setQuantity(orderCreateDto.getQuantity());
        order.setCustomer(this.customerService.getOne(orderCreateDto.getCustomerId()));
        order.setStatus(OrderStatus.PENDING);
        order.setCreatedAt(this.createOrderDate()); // for seeding data purpose only
        return order;
    }

    private Order orderUpdateDto2order(OrderUpdateDto orderUpdateDto, Order order) throws NotFoundException {
        order.setQuantity(orderUpdateDto.getQuantity());
        order.setStatus(orderUpdateDto.getStatus());
        order.setCustomer(this.customerService.getOne(orderUpdateDto.getCustomerId()));
        return order;
    }

    public List<Order> getAll() {
        System.out.println(this.orderRepo.findAllSortByPremiumAndCreatedAt());
        return this.orderRepo.findAllSortByPremiumAndCreatedAt();
    }

    public Order create(OrderCreateDto createDto) throws NotFoundException {
        Customer customer = this.customerService.getOne(createDto.getCustomerId());
        Optional<Order> foundOrder = this.orderRepo.findByCustomer(customer);
        if (foundOrder.isPresent()) {
            throw new EntityExistsException("customer has a current order");
        }
        Order order = this.orderCreateDto2order(createDto, new Order());
        return this.orderRepo.save(order);
    }

    public Order getOne(Long id) throws NotFoundException {
        return this.orderRepo.findById(id)
                .orElseThrow(() -> new NotFoundException(Order.class.getSimpleName() + " not found"));
    }

    public Order updateOne(Long id, OrderUpdateDto orderUpdateDto) throws NotFoundException {
        Order oldEntity = this.getOne(id);
        Order newEntity = this.orderUpdateDto2order(orderUpdateDto, oldEntity);
        return this.orderRepo.save(newEntity);
    }

    public void delete(Long id) {
        this.orderRepo.deleteById(id);
    }

    private int randomInRange(int from, int to) {
        return ThreadLocalRandom.current().nextInt(from, to + 1);
    }

    private Date createOrderDate() {
        LocalDate minDay = LocalDate.of(2019, 1, 1);
        LocalDate maxDay = LocalDate.of(2021, 1, 1);
        return new RandomDate(minDay, maxDay).nextDate();
    }

    public Order getCurrentOrder(Long customerId) throws NotFoundException {
        List<Order> orders =
                this.orderRepo.findAllSortByPremiumAndCreatedAt()
                        .stream()
                        .filter(order -> order.getCustomer().getId() == customerId)
                        .collect(Collectors.toList());
        if (orders.isEmpty()) {
            throw new NotFoundException("order not found");
        }
        return orders.get(0);
    }

    public List<Order> getNextDelivery() {
        return this.orderRepo.findAllSortByPremiumAndCreatedAt()
                .stream()
                .filter(order -> order.getWaitTime() <= this.CART_MAX_SIZE)
                .collect(Collectors.toList());
    }

    public Order cancelOrder(Long id) throws NotFoundException {
        Customer customer = this.customerService.getOne(id);
        Optional<Order> orderOptional = this.orderRepo.findByCustomer(customer);
        if (orderOptional.isEmpty()) {
            throw new NotFoundException("customer doesn't have any order");
        }
        Order foundOrder = orderOptional.get();
        foundOrder.setStatus(OrderStatus.CANCELLED);
        return this.orderRepo.save(foundOrder);
    }

    @Transactional
    public List<Order> handleNextDelivery() {
        List<Order> doneOrders = this.getNextDelivery()
                .stream().map(order -> {
                    order.setStatus(OrderStatus.DONE);
                    return order;
                })
                .collect(Collectors.toList());
        return (List<Order>) this.orderRepo.saveAll(doneOrders);
    }
}
