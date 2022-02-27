package com.example.crud_project.Order;

import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Getter
public class UpdateOrder {
    private Integer id;
    private String title;
    private String description;
    private Date deadline;
    private Date dead_time;
    private String knowledge;
    private Integer payment;
    private String payment_type;
}
