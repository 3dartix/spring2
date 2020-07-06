package ru.geekbrains.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.geekbrains.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
