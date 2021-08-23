package com.example.donut.customer.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class CustomerUpdateDto extends CustomerDto {
    private Date createdAt;
}
