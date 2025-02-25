package com.kaoutar.bookify.Bookify.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = true)
    private String description;
    @Column(nullable = false)
    private String coverImage;
//    //add a publication date
//    @Column(nullable = false)
//    private LocalDate publicationDate;
    @Column(nullable = true)
    private String language;
    @ElementCollection
    @CollectionTable(name = "book_genres", joinColumns = @JoinColumn(name = "book_id"))
    @Column(name = "genres")
    private List<String> genres;

    // Add a created at field
    @Column(nullable = false)
    private LocalDate createdAt = LocalDate.now();

    @ManyToOne
    @JsonIgnore
    private User author;

    // Store all reviews for the book
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    private List<Review> reviews = new ArrayList<>();

    // Average of all user reviews
    private Double averageReview = 0.0;

    public void updateAverageReview() {
        if (!reviews.isEmpty()) {
            this.averageReview = reviews.stream()
                    .mapToDouble(Review::getRating)
                    .average()
                    .orElse(0.0);
        } else {
            this.averageReview = 0.0; // No reviews
        }
    }

    // Getters and Setters...
}
