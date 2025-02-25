package com.kaoutar.bookify.Bookify.controller;


import com.kaoutar.bookify.Bookify.model.Book;
import com.kaoutar.bookify.Bookify.model.User;
import com.kaoutar.bookify.Bookify.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/users")
@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<User> authenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        return ResponseEntity.ok(currentUser);
    }

    @GetMapping("/")
    public ResponseEntity<List<User>> allUsers() {
        List<User> users = userService.allUsers();
        return ResponseEntity.ok(users);
    }

    // ✅ Add Book to a Category
    @PostMapping("/{userId}/books/{bookId}/add") // add?category=wantToRead...
    public ResponseEntity<String> addBookToCategory(
            @PathVariable Long userId,
            @PathVariable Long bookId,
            @RequestParam String category) {
        userService.addBookToCategory(userId, bookId, category);
        return ResponseEntity.ok("Book added to " + category);
    }

    // ✅ Move Book to "Read"
    @PostMapping("/{userId}/books/{bookId}/moveToRead")
    public ResponseEntity<String> moveFromCat1ToCat2(@PathVariable Long userId, @PathVariable Long bookId) {
        userService.moveFromCat1ToCat2(userId, bookId);
        return ResponseEntity.ok("Book moved to 'Read' category");
    }

    @GetMapping("/{userId}/books/{category}")
    public List<Book> getBooksByCategory(@PathVariable Long userId, @PathVariable String category) {
        return userService.getBooksByCategory(userId, category);
    }


}
