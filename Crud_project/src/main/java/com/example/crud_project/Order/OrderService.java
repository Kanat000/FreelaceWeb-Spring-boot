package com.example.crud_project.Order;

import com.example.crud_project.Cart.Cart;
import com.example.crud_project.Cart.CartRepository;
import com.example.crud_project.user.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class OrderService {
    private final OrderRepository repository;
    private final CartRepository cartRepository;
    public List<Order> listAll(){
        return (List<Order>) repository.findAll();
    }

    public List<Order> getOrdersByKeyword(String keyword){
        return repository.getOrdersByKeyword(keyword);
    }

    public List<Order> listAllOfUser(User user){
        return repository.getOrdersByUser(user);
    }

    public void save(Order order, User user) {
        order.setUser(user);
        repository.save(order);
    }

    public Boolean ExistsInCart(User user, Order order){
        List<Cart> listCart = cartRepository.GetCartByFKs(user.getId(),order.getId());
        if(listCart.isEmpty()){
            return false;
        }
        return true;
    }


    public Order getOrderById(Integer id){
        return repository.getOrderById(id);
    }

    public void deleteOrder(Order order) throws OrderNotFoundException {
        repository.delete(order);
    }

    public void updateOrder(Order order,UpdateOrder updateOrder){
        if(updateOrder.getTitle() != null){
            order.setTitle(updateOrder.getTitle());
        }
        if(updateOrder.getDescription() != null){
            order.setDescription(updateOrder.getDescription());
        }
        if(updateOrder.getDeadline() != null){
            order.setDeadline(updateOrder.getDeadline());
        }
        if(updateOrder.getDead_time() != null){
            order.setDead_time(updateOrder.getDead_time());
        }
        if(updateOrder.getDead_time() != null){
            order.setDead_time(updateOrder.getDead_time());
        }
        if(updateOrder.getKnowledge() != null){
            order.setKnowledge(updateOrder.getKnowledge());
        }
        if(updateOrder.getPayment() != null){
            order.setPayment(updateOrder.getPayment());
        }
        if(updateOrder.getPayment_type() != null){
            order.setPayment_type(updateOrder.getPayment_type());
        }
        repository.save(order);
    }
}
