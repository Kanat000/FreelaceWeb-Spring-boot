package com.example.crud_project.Order;

public class OrderNotFoundException extends Throwable {
    public OrderNotFoundException(String message) {
        super(message);
    }
}
