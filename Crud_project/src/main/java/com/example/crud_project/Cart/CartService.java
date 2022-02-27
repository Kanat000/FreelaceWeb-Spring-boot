package com.example.crud_project.Cart;

import com.example.crud_project.Order.Order;
import com.example.crud_project.user.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class CartService {
    private final CartRepository cartRepository;
    public void SaveToCart(User user, Order order){
        Cart cart = new Cart();
        cart.setUser(user);
        cart.setOrder(order);
        cartRepository.save(cart);
    }
    public void deleteByOrder(Order order){
        cartRepository.deleteCartByOrder(order);
    }
    public List<Cart> GetSavedOrdersByUser(User user){
        return cartRepository.getCartsByUser(user);
    }
    public void DeleteFromCart(User user,Order order){
        cartRepository.DeleteFromCart(user.getId(), order.getId());
    }
}
