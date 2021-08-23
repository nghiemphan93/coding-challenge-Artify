package com.example.donut.order;

import com.example.donut.DonutApplication;
import com.example.donut.TestHelper;
import com.example.donut.customer.CustomerService;
import com.example.donut.customer.dtos.CustomerCreateDto;
import com.example.donut.order.dtos.OrderCreateDto;
import com.example.donut.order.dtos.OrderUpdateDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = DonutApplication.class)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class OrderRestControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private OrderService orderService;
    private String baseUrl = "/api/v1/orders";

    @Test
    public void shouldGetAllReturnLength2() throws Exception {
        // given
        CustomerCreateDto entity1 = new CustomerCreateDto();
        entity1.setId(2L);
        this.customerService.create(entity1);
        CustomerCreateDto entity2 = new CustomerCreateDto();
        entity2.setId(3L);
        this.customerService.create(entity2);

        OrderCreateDto orderCreateDto1 = new OrderCreateDto();
        orderCreateDto1.setCustomerId(2L);
        orderCreateDto1.setQuantity(10);
        this.orderService.create(orderCreateDto1);
        OrderCreateDto orderCreateDto2 = new OrderCreateDto();
        orderCreateDto2.setCustomerId(3L);
        orderCreateDto2.setQuantity(20);
        this.orderService.create(orderCreateDto2);

        // when -> then
        this.mockMvc
                .perform(get(this.baseUrl))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2));
    }

    @Test
    public void shouldGetOneReturnSuccess() throws Exception {
        // given
        CustomerCreateDto entity1 = new CustomerCreateDto();
        entity1.setId(4L);
        this.customerService.create(entity1);

        OrderCreateDto orderCreateDto1 = new OrderCreateDto();
        orderCreateDto1.setCustomerId(4L);
        orderCreateDto1.setQuantity(10);
        Order order = this.orderService.create(orderCreateDto1);

        // when
        this.mockMvc
                .perform(get(this.baseUrl + "/" + order.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldCreateOneReturnSuccess() throws Exception {
        // given
        CustomerCreateDto entity1 = new CustomerCreateDto();
        entity1.setId(5L);
        this.customerService.create(entity1);

        OrderCreateDto orderCreateDto1 = new OrderCreateDto();
        orderCreateDto1.setCustomerId(5L);
        orderCreateDto1.setQuantity(10);

        // when
        this.mockMvc
                .perform(post(this.baseUrl)
                        .content(TestHelper.asJsonString(orderCreateDto1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void shouldUpdateOneReturnSuccess() throws Exception {
        // given
        CustomerCreateDto entity1 = new CustomerCreateDto();
        entity1.setId(7L);
        this.customerService.create(entity1);

        OrderCreateDto orderCreateDto1 = new OrderCreateDto();
        orderCreateDto1.setCustomerId(7L);
        orderCreateDto1.setQuantity(10);
        Order order = this.orderService.create(orderCreateDto1);
        OrderUpdateDto orderUpdateDto = new OrderUpdateDto();
        orderUpdateDto.setStatus(order.getStatus());
        orderUpdateDto.setCustomerId(order.getCustomer().getId());
        orderUpdateDto.setQuantity(20);

        // when
        this.mockMvc
                .perform(put(this.baseUrl + "/" + order.getId())
                        .content(TestHelper.asJsonString(orderUpdateDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldDeleteOneReturnSuccess() throws Exception {
        // given
        CustomerCreateDto customer1 = new CustomerCreateDto();
        customer1.setId(9L);
        this.customerService.create(customer1);
        OrderCreateDto orderCreateDto1 = new OrderCreateDto();
        orderCreateDto1.setCustomerId(9L);
        orderCreateDto1.setQuantity(10);
        Order order = this.orderService.create(orderCreateDto1);

        this.mockMvc
                .perform(delete(this.baseUrl + "/" + order.getId()))
                .andExpect(status().isAccepted());
    }
}
