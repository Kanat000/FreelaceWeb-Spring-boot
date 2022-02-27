package com.example.crud_project.Order;

import com.example.crud_project.user.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface OrderRepository extends CrudRepository<Order, Integer> {

    List<Order> getOrdersByUser(User user);

    Order getOrderById(Integer id);

    @Transactional
    @Modifying
    @Query("Select o from Order o where o.title like %?1% or o.description like %?1% or o.knowledge like %?1%")
    List<Order> getOrdersByKeyword(String keyword);
}
