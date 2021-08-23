package com.example.donut.order.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class OrderCreateDto {
    @NotNull
    private int quantity;
    @NotNull
    private Long customerId;
}
