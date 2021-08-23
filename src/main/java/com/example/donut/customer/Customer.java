package com.example.donut.customer;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@Table(name = "customers")
public class Customer {
    @Id
    @Column(name = "id", unique = true, nullable = false)
    private Long id;
    private Date createdAt;
    private boolean isPremium;
}
