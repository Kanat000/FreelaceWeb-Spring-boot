package com.example.crud_project.Cart;

import com.example.crud_project.Order.Order;
import com.example.crud_project.user.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface CartRepository extends CrudRepository<Cart, Long> {

    List<Cart> getCartsByUser(User user);

    void deleteCartByOrder(Order order);
    @Transactional
    @Modifying
    @Query("Select c from Cart as c where c.user.id = ?1 and c.order.id = ?2")
    List<Cart> GetCartByFKs(Long user, Integer order);


    @Transactional
    @Modifying
    @Query("Delete from Cart as c where c.user.id = ?1 and c.order.id = ?2")
    void DeleteFromCart(Long user, Integer order);

}
