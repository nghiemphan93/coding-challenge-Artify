package com.example.donut.order.dtos;

import com.example.donut.order.OrderStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@NoArgsConstructor
public class OrderUpdateDto extends OrderDto{
    @NotNull
    private OrderStatus status;
}
