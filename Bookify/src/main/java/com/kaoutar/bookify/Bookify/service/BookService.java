package com.kaoutar.bookify.Bookify.service;

import com.kaoutar.bookify.Bookify.model.Book;
import com.kaoutar.bookify.Bookify.model.Review;
import com.kaoutar.bookify.Bookify.model.Role;
import com.kaoutar.bookify.Bookify.model.User;
import com.kaoutar.bookify.Bookify.repository.BookRepository;
import com.kaoutar.bookify.Bookify.repository.ReviewRepository;
import com.kaoutar.bookify.Bookify.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    @Autowired
    public BookService(BookRepository bookRepository, ReviewRepository reviewRepository, UserRepository userRepository) {
        this.bookRepository = bookRepository;
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
    }

    // ✅ Add a Review to a Book
    public void addReview(Long userId, Long bookId, int rating, String comment) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        // Create a new review
        Review review = new Review();
        review.setUser(user);
        review.setBook(book);
        review.setRating(rating);
        review.setComment(comment);

        reviewRepository.save(review);

        // Add the review to the book and update the average
        book.getReviews().add(review);
        book.updateAverageReview();
        bookRepository.save(book);
    }

    @Transactional
    public void publishBook(Long authorId, Book book) {
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new RuntimeException("Author not found"));

        System.out.println("Authenticated Author: " + author.getUsername());
        System.out.println("Author Role: " + author.getRole());

        if (!author.getRole().equals(Role.AUTHOR)) {
            throw new RuntimeException("Only authors can publish books!");
        }

        // Debug: Log the author and book before saving
        System.out.println("Author: " + author.getUsername());
        System.out.println("Book to save: " + book);

        book.setAuthor(author);

        if (book.getCoverImage() == null || book.getCoverImage().isEmpty()) {
            throw new RuntimeException("Cover image is required");
        }

        try {
            // Save the book with genres
            bookRepository.save(book);
            System.out.println("Book saved successfully: " + book);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error saving book: " + e.getMessage());
        }

        // Optionally, you can manage a list of books in the author (like you've done earlier)
        author.getMyPublishedBooks().add(book);
        userRepository.save(author); // Save the author with the updated book list
    }


    public List<Book> getBooksByGenres(String category) {
        return bookRepository.findByGenres(category);
    }

    public List<Book> getLatestBooks() {
        // Assuming you have a 'createdAt' field that tracks the date the book was added.
        return bookRepository.findTop9ByOrderByCreatedAtDesc(); // Fetches the last 9 books based on the createdAt field
    }





}
