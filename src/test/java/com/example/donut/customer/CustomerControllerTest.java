package com.example.donut.customer;

import com.example.donut.DonutApplication;
import com.example.donut.TestHelper;
import com.example.donut.customer.dtos.CustomerCreateDto;
import com.example.donut.customer.dtos.CustomerUpdateDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = DonutApplication.class)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CustomerControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CustomerService customerService;
    private String baseUrl = "/api/v1/customers";

    @Test
    public void shouldGetAllReturnLength2() throws Exception {
        // given
        CustomerCreateDto entity1 = new CustomerCreateDto();
        entity1.setId(2L);
        this.customerService.create(entity1);
        CustomerCreateDto entity2 = new CustomerCreateDto();
        entity2.setId(25999L);
        this.customerService.create(entity2);

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
        entity1.setId(2L);
        this.customerService.create(entity1);

        // when
        this.mockMvc
                .perform(get(this.baseUrl + "/2"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldCreateOneReturnSuccess() throws Exception {
        // given
        CustomerCreateDto entity1 = new CustomerCreateDto();
        entity1.setId(2L);

        // when
        this.mockMvc
                .perform(post(this.baseUrl)
                        .content(TestHelper.asJsonString(entity1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void shouldCreateOneThrowNotValidId() throws Exception {
        // given
        CustomerCreateDto entity1 = new CustomerCreateDto();
        entity1.setId(25999L);

        // when
        this.mockMvc
                .perform(post(this.baseUrl)
                        .content(TestHelper.asJsonString(entity1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(result -> assertTrue(result.getResolvedException().getMessage().contains("customer id must be from")));
    }

    @Test
    public void shouldUpdateOneReturnSuccess() throws Exception {
        // given
        CustomerCreateDto entity1 = new CustomerCreateDto();
        entity1.setId(2L);
        Customer updatedEntity = this.customerService.create(entity1);
        CustomerUpdateDto updateDto = new CustomerUpdateDto();
        updateDto.setId(updatedEntity.getId());
        updateDto.setCreatedAt(new Date());

        // when
        this.mockMvc
                .perform(put(this.baseUrl + "/2")
                        .content(TestHelper.asJsonString(updateDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldUpdatedOneThrowNotValidId() throws Exception {
        // given
        CustomerCreateDto entity1 = new CustomerCreateDto();
        entity1.setId(2L);
        Customer updatedEntity = this.customerService.create(entity1);
        CustomerUpdateDto updateDto = new CustomerUpdateDto();
        updateDto.setId(25999L);
        updateDto.setCreatedAt(updatedEntity.getCreatedAt());

        // when
        this.mockMvc
                .perform(put(this.baseUrl + "/2")
                        .content(TestHelper.asJsonString(updateDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(result -> assertTrue(result.getResolvedException().getMessage().contains("customer id must be from")));
    }


    @Test
    public void shouldDeleteOneReturnSuccess() throws Exception {
        // given
        CustomerCreateDto customer1 = new CustomerCreateDto();
        customer1.setId(2L);
        this.customerService.create(customer1);

        this.mockMvc
                .perform(delete(this.baseUrl + "/2"))
                .andExpect(status().isAccepted());
    }
}
