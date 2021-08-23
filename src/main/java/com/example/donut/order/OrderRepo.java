package com.example.donut.order;

import com.example.donut.customer.Customer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepo extends CrudRepository<Order, Long> {
    @Query(value = "" +
            "SELECT o.id, o.created_at, o.quantity, o.status, o.customer_id, " +
            "SUM(o.quantity) OVER (ORDER BY c.is_premium DESC, o.created_at ASC) as wait_time, " +
            "ROW_NUMBER() OVER (ORDER BY c.is_premium DESC, o.created_at ASC) current_position\n" +
            "FROM orders o, customers c\n" +
            "WHERE o.customer_id = c.id\n" +
            "AND o.status = 0\n" +
            "ORDER BY c.is_premium DESC, o.created_at ASC;", nativeQuery = true)
    List<Order> findAllSortByPremiumAndCreatedAt();

    Optional<Order> findByCustomer(Customer customer);
}
