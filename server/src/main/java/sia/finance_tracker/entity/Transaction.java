//package sia.finance_tracker.entity;
//
//import jakarta.persistence.*;
//import java.time.LocalDate;
//
//@Entity
//public class Transaction {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private String type; // "income" or "expense"
//    private Double amount;
//    private LocalDate date;
//    private String description;
//
//    // Foreign key to User
//    @ManyToOne
//    @JoinColumn(name = "user_id", nullable = false)
//    private User user;
//
//    // Foreign key to Category.css
//    @ManyToOne
//    @JoinColumn(name = "category_id", nullable = false)
//    private Category.css category;
//
//    // Constructors, Getters, Setters
//}

package sia.finance_tracker.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type; // "income" or "expense"
    private Double amount;
    private LocalDate date;
    private String description;

    // Foreign key to User
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Foreign key to Category.css
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    // No-argument constructor (required by JPA)
    public Transaction() {}

    // Parameterized constructor
    public Transaction(String type, Double amount, LocalDate date, String description, User user, Category category) {
        this.type = type;
        this.amount = amount;
        this.date = date;
        this.description = description;
        this.user = user;
        this.category = category;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
}

