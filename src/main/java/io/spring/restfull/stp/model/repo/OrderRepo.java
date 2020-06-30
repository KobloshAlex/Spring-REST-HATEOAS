package io.spring.restfull.stp.model.repo;

import io.spring.restfull.stp.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepo extends JpaRepository<Order, Long> {}
