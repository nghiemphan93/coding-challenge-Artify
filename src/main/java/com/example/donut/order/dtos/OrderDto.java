package com.example.donut.order.dtos;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public abstract class OrderDto {
    @NotNull
    private int quantity;
    @NotNull
    private Long customerId;
}
