package com.ved.SimpleEcommerceAPI.repository;

import com.ved.SimpleEcommerceAPI.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
