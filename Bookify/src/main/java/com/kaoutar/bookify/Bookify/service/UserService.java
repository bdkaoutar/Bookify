package com.kaoutar.bookify.Bookify.service;


import com.kaoutar.bookify.Bookify.model.Book;
import com.kaoutar.bookify.Bookify.model.User;
import com.kaoutar.bookify.Bookify.repository.BookRepository;
import com.kaoutar.bookify.Bookify.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    public UserService(UserRepository userRepository, EmailService emailService, BookRepository bookRepository) {
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }

    public List<User> allUsers() {
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        return users;
    }

    // Add a Book to a Category
    public void addBookToCategory(Long userId, Long bookId, String category) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        switch (category.toLowerCase()) {
            case "read":
                user.getRead().add(book);
                break;
            case "currentlyreading":
                user.getCurrentlyReading().add(book);
                break;
            case "wanttoread":
                user.getWantToRead().add(book);
                break;
            default:
                throw new IllegalArgumentException("Invalid category: " + category);
        }

        userRepository.save(user);
    }

    // Move a Book from one category to another
    public void moveFromCat1ToCat2(Long userId, Long bookId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        if (user.getRead().remove(book)) {
            user.getCurrentlyReading().add(book);
        } else if (user.getCurrentlyReading().remove(book)) {
            user.getWantToRead().add(book);
        } else if (user.getWantToRead().remove(book)) {
            user.getRead().add(book);
        } else {
            throw new RuntimeException("Book not found in any category");
        }

        userRepository.save(user);
    }

    //add get user by email
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public List<Book> getBooksByCategory(Long userId, String category) {
        // Find user by their ID
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        // Return the appropriate category of books based on the passed category string
        return switch (category) {
            case "currentlyReading" -> user.getCurrentlyReading();
            case "read" -> user.getRead();
            case "wantToRead" -> user.getWantToRead();
            default -> throw new RuntimeException("Category not found");
        };
    }


//    public void moveToRead(Long userId, Long bookId) {
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        Book book = bookRepository.findById(bookId)
//                .orElseThrow(() -> new RuntimeException("Book not found"));
//
//        if (user.getCurrentlyReading().remove(book)) {
//            user.getRead().add(book);
//            userRepository.save(user);
//        } else {
//            throw new RuntimeException("Book not found in Currently Reading list");
//        }
//    }




}
