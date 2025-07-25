package com.ved.SimpleEcommerceAPI.repository;

import com.ved.SimpleEcommerceAPI.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
