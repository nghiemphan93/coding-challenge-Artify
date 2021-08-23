package com.example.donut.customer.dtos;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

@Data
public abstract class CustomerDto {
    @NotNull(message = "Id is not allowed to be null")
    @Range(min = 1, max = 20000, message = "customer id must be from 1 to 20000")
    private Long id;
}
