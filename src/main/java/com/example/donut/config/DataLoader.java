package com.example.donut.config;

import com.example.donut.customer.Customer;
import com.example.donut.customer.CustomerRepo;
import com.example.donut.customer.CustomerService;
import com.example.donut.customer.dtos.CustomerCreateDto;
import com.example.donut.order.Order;
import com.example.donut.order.OrderRepo;
import com.example.donut.order.OrderService;
import com.example.donut.order.dtos.OrderCreateDto;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Component
@AllArgsConstructor
public class DataLoader implements ApplicationRunner {
    private static final Logger logger = LoggerFactory.getLogger(DataLoader.class);
    private final OrderService orderService;
    private final OrderRepo orderRepo;
    private final CustomerService customerService;
    private final CustomerRepo customerRepo;
    private final int MAX_PREMIUM_CUSTOMERS = 10;
    private final int MAX_NORMAL_CUSTOMERS = 20;
    private final int MAX_PREMIUM_ORDERS = 10;
    private final int MAX_NORMAL_ORDERS = 20;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (!DataLoader.isJUnitTest()) {
            this.createCustomers();
            this.createOrders();
        }
    }

    private int randomInRange(int from, int to) {
        return ThreadLocalRandom.current().nextInt(from, to + 1);
    }

    public static boolean isJUnitTest() {
        for (StackTraceElement element : Thread.currentThread().getStackTrace()) {
            if (element.getClassName().startsWith("org.junit.")) {
                return true;
            }
        }
        return false;
    }

    private void createCustomers() throws NotFoundException {
        if (((List<Customer>) this.customerRepo.findAll()).isEmpty()) {
            for (int i = 0; i < this.MAX_NORMAL_CUSTOMERS; i++) {
                CustomerCreateDto customerCreateDto = new CustomerCreateDto();
                customerCreateDto.setId(1050L + i);
                this.customerService.create(customerCreateDto);
            }
            for (int i = 0; i < this.MAX_PREMIUM_CUSTOMERS; i++) {
                CustomerCreateDto customerCreateDto = new CustomerCreateDto();
                customerCreateDto.setId(20L + i);
                this.customerService.create(customerCreateDto);
            }
        }
        logger.info("created customers...");
    }

    private void createOrders() throws NotFoundException {
        if (((List<Order>) this.orderRepo.findAll()).isEmpty()) {
            for (int i = 0; i < this.MAX_PREMIUM_ORDERS; i++) {
                OrderCreateDto orderCreateDto = new OrderCreateDto();
                orderCreateDto.setCustomerId(20L + i);
                orderCreateDto.setQuantity(this.randomInRange(2, 30));
                this.orderService.create(orderCreateDto);
            }
            for (int i = 0; i < this.MAX_NORMAL_ORDERS; i++) {
                OrderCreateDto orderCreateDto = new OrderCreateDto();
                orderCreateDto.setQuantity(this.randomInRange(2, 30));
                orderCreateDto.setCustomerId(1050L + i);
                this.orderService.create(orderCreateDto);
            }
        }
        logger.info("created orders...");
    }
}
