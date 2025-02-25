package com.kaoutar.bookify.Bookify.controller;

import com.kaoutar.bookify.Bookify.model.Book;
import com.kaoutar.bookify.Bookify.model.Role;
import com.kaoutar.bookify.Bookify.model.User;
import com.kaoutar.bookify.Bookify.repository.BookRepository;
import com.kaoutar.bookify.Bookify.repository.UserRepository;
import com.kaoutar.bookify.Bookify.service.BookService;
import com.kaoutar.bookify.Bookify.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1/books")
public class BookController {

    private final BookService bookService;
    private final BookRepository bookRepository;
    private final UserService userService;
    private final UserRepository userRepository;

    @Autowired
    public BookController(BookService bookService, BookRepository bookRepository, UserService userService, UserRepository userRepository) {
        this.bookService = bookService;
        this.bookRepository = bookRepository;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    // ✅ Add Review to a Book
    @PostMapping("/{bookId}/review")
    public ResponseEntity<String> addReview(
            @PathVariable Long bookId,
            @RequestParam Long userId,
            @RequestParam int rating,
            @RequestParam(required = false) String comment) {
        bookService.addReview(userId, bookId, rating, comment);
        return ResponseEntity.ok("Review added successfully!");
    }

    // Add this method to the BookController to fetch the authorId
    @GetMapping("/get-author-id")
    public ResponseEntity<Long> getAuthorIdByEmail(Authentication authentication) {
        User user = (User) authentication.getPrincipal(); // Get User object from authentication
        String email = user.getEmail(); // Extract the email from the User object


        return ResponseEntity.ok(user.getId());  // Return the authorId
    }





    @GetMapping("/genre/{genre}")
    public ResponseEntity<List<Book>> getBooksByGenres(@PathVariable String genre) {
        List<Book> books = bookService.getBooksByGenres(genre);
        return ResponseEntity.ok(books);
    }

    // Fetch last 9 books
    @GetMapping("/latest")
    public ResponseEntity<List<Book>> getLatestBooks() {
        List<Book> books = bookService.getLatestBooks();  // Assuming bookService has a method to fetch the last 9 books
        return ResponseEntity.ok(books);
    }

    @PostMapping("/publish/{authorId}")
    public ResponseEntity<Book> publishBook(
            @PathVariable Long authorId,
            @RequestBody Book book
    ) {
        bookService.publishBook(authorId, book);
        return ResponseEntity.ok(book);
    }

    //get books by category
    @GetMapping("/{userId}/books/{category}")
    public ResponseEntity<List<Book>> getBooksByCategory(
            @PathVariable Long userId,
            @PathVariable String category) {

        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        List<Book> books;
        switch (category) {
            case "wantToRead":
                books = user.getWantToRead();
                break;
            case "currentlyReading":
                books = user.getCurrentlyReading();
                break;
            case "read":
                books = user.getRead();
                break;
            default:
                throw new RuntimeException("Invalid category");
        }

        return ResponseEntity.ok(books);
    }




}

