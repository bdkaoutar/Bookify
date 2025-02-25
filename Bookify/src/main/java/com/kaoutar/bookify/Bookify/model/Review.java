package com.kaoutar.bookify.Bookify.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user; // The user who submitted the review

    @ManyToOne
    private Book book; // The book being reviewed

    private int rating; // Rating out of 5 or 10

    private String comment; // Optional comment

    // Getters and Setters...
}
