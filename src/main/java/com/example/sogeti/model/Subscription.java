package com.example.sogeti.model;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "subscriptions")
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user; // connection with user

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category; // connection with category

    @ManyToOne
    @JoinColumn(name = "shared_with_id", nullable = true)
    private User sharedWith;
    private Integer remainingContent;
    private Double price;
    private LocalDate startDate;
    private LocalDate startPaymentDate;

    public Subscription() {
    }

    public Subscription(User user, Category category, Integer remainingContent, Double price, LocalDate startDate) {
        this.user = user;
        this.category = category;
        this.remainingContent = remainingContent;
        this.price = price;
        this.startDate = startDate;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }

    public Category getCategory() {
        return category;
    }
    public void setCategory(Category category) {
        this.category = category;
    }

    public Integer getRemainingContent() {
        return remainingContent;
    }
    public void setRemainingContent(Integer remainingContent) {
        this.remainingContent = remainingContent;
    }

    public Double getPrice() {
        return price;
    }
    public void setPrice(Double price) {
        this.price = price;
    }

    public LocalDate getStartDate() {
        return startDate;
    }
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getStartPaymentDate() {return startPaymentDate;}
    public void setStartPaymentDate(LocalDate startPaymentDate) {this.startPaymentDate = startPaymentDate;}

    public User getSharedWith() {return sharedWith;}
    public void setSharedWith(User sharedWith) {this.sharedWith = sharedWith;}
}
