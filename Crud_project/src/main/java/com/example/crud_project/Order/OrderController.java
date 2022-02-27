package com.example.crud_project.Order;

import com.example.crud_project.Cart.CartRepository;
import com.example.crud_project.user.UpdateUser;
import com.example.crud_project.user.User;
import com.example.crud_project.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;

@Controller
@AllArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class OrderController {
    private final OrderService orderService;
    private final UserService userService;
    private final CartRepository cartRepository;

    @GetMapping("/")
    public String OrderList(Model model, Authentication authentication){
        List<Order> listOrders = orderService.listAll();
        User user = userService.getUserByUsername(authentication.getName());
        HashMap<Integer,Boolean> hashArr = new HashMap<>(2);
        for (Order order : listOrders) {
            hashArr.put(order.getId(), orderService.ExistsInCart(user, order));
        }
        model.addAttribute("listOrders", listOrders);
        model.addAttribute("user_id", user.getId());
        model.addAttribute("cartHash",hashArr);
        return "index";
    }
    @GetMapping("/order/get")
    public String GetOrderById(Model model, Authentication authentication, @RequestParam Integer order_id){
        User user = userService.getUserByUsername(authentication.getName());
        Order order = orderService.getOrderById(order_id);
        model.addAttribute("order",order);
        model.addAttribute("user",user);
        return "OrderPage";
    }

    @GetMapping("/order/delete")
    public String DeleteOrder(Authentication authentication, @RequestParam Integer order_id) throws OrderNotFoundException {
        User user = userService.getUserByUsername(authentication.getName());
        Order order = orderService.getOrderById(order_id);
        if(order.getUser().getId().equals(user.getId())){
            cartRepository.deleteCartByOrder(order);
            orderService.deleteOrder(order);
        }
        return "redirect:/";
    }

    @PostMapping("/order/update")
    public String UpdateOrder(Authentication authentication, @RequestBody UpdateOrder updateOrder){
        User user = userService.getUserByUsername(authentication.getName());
        Order order = orderService.getOrderById(updateOrder.getId());
        if(order.getUser().getId().equals(user.getId())){
            orderService.updateOrder(order,updateOrder);
        }
        return "redirect:/";
    }

    @GetMapping("/search")
    public String SearchOrder(Model model,Authentication authentication, @RequestParam String search){
        List<Order> listOrders = orderService.getOrdersByKeyword(search);
        User user = userService.getUserByUsername(authentication.getName());
        HashMap<Integer,Boolean> hashArr = new HashMap<>(2);
        for (Order order : listOrders) {
            hashArr.put(order.getId(), orderService.ExistsInCart(user, order));
        }
        model.addAttribute("listOrders", listOrders);
        model.addAttribute("user_id", user.getId());
        model.addAttribute("cartHash",hashArr);
        return "index";
    }

    @GetMapping("/addOrder")
    public String newOrder(Model model){
        model.addAttribute("order",new Order());
        return "AddOrder";
    }


    @GetMapping("/profile")
    public String getProfile(Model model, Authentication authentication){
        User user = userService.getUserByUsername(authentication.getName());
        List<Order> listOrders = orderService.listAllOfUser(user);
        HashMap<Integer,Boolean> hashArr = new HashMap<>(2);
        for (Order order : listOrders) {
            hashArr.put(order.getId(), orderService.ExistsInCart(user, order));
        }
        model.addAttribute("user", user);
        model.addAttribute("listOrders",listOrders);
        model.addAttribute("cartHash",hashArr);
        return "Profile";
    }
    @PostMapping("/profile/update")
    public String getProfile(@RequestBody UpdateUser updateUser, Authentication authentication){
        User user = userService.getUserByUsername(authentication.getName());
        userService.update(user,updateUser);
        return "redirect:/profile";
    }

    @PostMapping("/profile/changeImage")
    public String changeImage(@RequestParam("image") MultipartFile image, Authentication authentication,Model model){
        User user = userService.getUserByUsername(authentication.getName());
        String prev_img = user.getImage();
        if(prev_img != null){
            if(!prev_img.equals("UserDefaultImage.jpg")){
                File prev_file = new File("C:/Users/USER/IdeaProjects/CRUD-Term-Project-main/CRUD-Term-Project-main/Crud_project/src/main/resources/static/img/"+prev_img);
                boolean deleted = prev_file.delete();
            }
        }
        try {
            user.setImage(image.getOriginalFilename());
            userService.saveUser(user);
            image.transferTo(new File("C:/Users/USER/IdeaProjects/CRUD-Term-Project-main/CRUD-Term-Project-main/Crud_project/src/main/resources/static/img/"+image.getOriginalFilename()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/profile";
    }

    @PostMapping("/order/save")
    public String  saveOrder(Order order, RedirectAttributes redirectAttributes, Authentication authentication){
        User user = userService.getUserByUsername(authentication.getName());
        orderService.save(order, user);
        redirectAttributes.addFlashAttribute("message", "The order has been saved successfully" );

        return "redirect:/";
    }

    /*@PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/order/delete/{order_id}")
    public String deleteOrder(@PathVariable("order_id") Integer order_id,RedirectAttributes redirectAttributes){
        try {
            orderService.deleteSneaker(order_id);
            redirectAttributes.addFlashAttribute("message", "The order Id "+ order_id +" has been deleted" );
        } catch (OrderNotFoundException e){
            redirectAttributes.addFlashAttribute("message", e.getMessage() );
        }
        return "redirect:/order";
    }*/
}
