package com.example.donut.customer;

import com.example.donut.DonutApplication;
import com.example.donut.customer.dtos.CustomerCreateDto;
import com.example.donut.order.OrderService;
import com.example.donut.order.dtos.OrderCreateDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = DonutApplication.class)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CustomerCustomControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private OrderService orderService;
    private String baseUrl = "/api/v1/customers";

    @Test
    public void shouldGetCurrentOrderReturnSuccess() throws Exception {
        // given
        CustomerCreateDto entity1 = new CustomerCreateDto();
        entity1.setId(2L);
        Customer customer = this.customerService.create(entity1);

        OrderCreateDto orderCreateDto = new OrderCreateDto();
        orderCreateDto.setCustomerId(customer.getId());
        orderCreateDto.setQuantity(10);
        this.orderService.create(orderCreateDto);

        // when
        this.mockMvc
                .perform(get(this.baseUrl + "/" + customer.getId() + "/currentOrder"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldCancelOrderReturnSuccess() throws Exception {
        // given
        CustomerCreateDto entity1 = new CustomerCreateDto();
        entity1.setId(2L);
        Customer customer = this.customerService.create(entity1);

        OrderCreateDto orderCreateDto = new OrderCreateDto();
        orderCreateDto.setCustomerId(customer.getId());
        orderCreateDto.setQuantity(10);
        this.orderService.create(orderCreateDto);

        // when
        this.mockMvc
                .perform(put(this.baseUrl + "/" + customer.getId() + "/cancelOrder"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
