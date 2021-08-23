package com.example.donut.order;

import com.example.donut.DonutApplication;
import com.example.donut.customer.Customer;
import com.example.donut.customer.CustomerService;
import com.example.donut.customer.dtos.CustomerCreateDto;
import com.example.donut.order.dtos.OrderCreateDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = DonutApplication.class)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class OrderCustomControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private OrderService orderService;
    private String baseUrl = "/api/v1/orders";

    @Test
    public void shouldNextDeliveryReturnFirst3Orders() throws Exception {
        // given
        CustomerCreateDto entity1 = new CustomerCreateDto();
        entity1.setId(1L);
        Customer customer1 = this.customerService.create(entity1);
        CustomerCreateDto entity2 = new CustomerCreateDto();
        entity2.setId(2L);
        Customer customer2 = this.customerService.create(entity2);
        CustomerCreateDto entity3 = new CustomerCreateDto();
        entity3.setId(3L);
        Customer customer3 = this.customerService.create(entity3);

        OrderCreateDto orderCreateDto1 = new OrderCreateDto();
        orderCreateDto1.setCustomerId(customer1.getId());
        orderCreateDto1.setQuantity(10);
        this.orderService.create(orderCreateDto1);
        OrderCreateDto orderCreateDto2 = new OrderCreateDto();
        orderCreateDto2.setCustomerId(customer2.getId());
        orderCreateDto2.setQuantity(20);
        this.orderService.create(orderCreateDto2);
        OrderCreateDto orderCreateDto3 = new OrderCreateDto();
        orderCreateDto3.setCustomerId(customer3.getId());
        orderCreateDto3.setQuantity(10);
        this.orderService.create(orderCreateDto3);

        // when -> then
        this.mockMvc
                .perform(get(this.baseUrl + "/nextDelivery"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(3));
    }

    @Test
    public void shouldHandleNextDeliveryReturnSuccess() throws Exception {
        // given
        CustomerCreateDto entity1 = new CustomerCreateDto();
        entity1.setId(1L);
        Customer customer1 = this.customerService.create(entity1);
        CustomerCreateDto entity2 = new CustomerCreateDto();
        entity2.setId(2L);
        Customer customer2 = this.customerService.create(entity2);
        CustomerCreateDto entity3 = new CustomerCreateDto();
        entity3.setId(3L);
        Customer customer3 = this.customerService.create(entity3);

        OrderCreateDto orderCreateDto1 = new OrderCreateDto();
        orderCreateDto1.setCustomerId(customer1.getId());
        orderCreateDto1.setQuantity(10);
        this.orderService.create(orderCreateDto1);
        OrderCreateDto orderCreateDto2 = new OrderCreateDto();
        orderCreateDto2.setCustomerId(customer2.getId());
        orderCreateDto2.setQuantity(20);
        this.orderService.create(orderCreateDto2);
        OrderCreateDto orderCreateDto3 = new OrderCreateDto();
        orderCreateDto3.setCustomerId(customer3.getId());
        orderCreateDto3.setQuantity(10);
        this.orderService.create(orderCreateDto3);

        // when -> then
        this.mockMvc
                .perform(put(this.baseUrl + "/handleNextDelivery"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}
