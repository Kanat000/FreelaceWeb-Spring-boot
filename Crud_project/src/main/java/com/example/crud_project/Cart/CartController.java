package com.example.crud_project.Cart;


import com.example.crud_project.Order.Order;
import com.example.crud_project.Order.OrderService;
import com.example.crud_project.user.User;
import com.example.crud_project.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@Controller
@AllArgsConstructor
@RequestMapping("/cart")
public class CartController {
    private final UserService userService;
    private final OrderService orderService;
    private final CartService cartService;

    @GetMapping("/")
    public String DisplaySavedOrders(Model model, Authentication authentication){
        User user = userService.getUserByUsername(authentication.getName());
        List<Cart> listCarts = cartService.GetSavedOrdersByUser(user);
        List<Order> listOrders = new ArrayList<>(2);
        HashMap<Integer,Boolean> hashArr = new HashMap<>(2);

        for (Cart cart:listCarts) {
            listOrders.add(cart.getOrder());
        }
        for (Order order : listOrders) {
            hashArr.put(order.getId(), orderService.ExistsInCart(user, order));
        }

        model.addAttribute("listOrders", listOrders);
        model.addAttribute("user_id", user.getId());
        model.addAttribute("cartHash",hashArr);

        return "cart";
    }

    @PostMapping("/save")
    public void SaveOrderToCart(@RequestBody CartRequest cartRequest){
        User user = userService.getUSerById((long) Integer.parseInt(cartRequest.getUser_id()));
        Order order = orderService.getOrderById(Integer.parseInt(cartRequest.getOrder_id()));
        cartService.SaveToCart(user,order);
    }
    @PostMapping("/delete")
    public void DeleteFromCart(@RequestBody CartRequest cartRequest){
        User user = userService.getUSerById((long) Integer.parseInt(cartRequest.getUser_id()));
        Order order = orderService.getOrderById(Integer.parseInt(cartRequest.getOrder_id()));
        cartService.DeleteFromCart(user,order);
    }
}
