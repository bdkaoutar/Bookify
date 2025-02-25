package com.kaoutar.bookify.Bookify.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true, nullable = false)
    private String username;
    // add first and last name
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    private boolean enabled;
    @Column(name = "verification_code")
    private String verificationCode;
    @Column(name = "verification_expiration")
    private LocalDateTime verificationExpiration;

    //getRole
    @Getter
    @Enumerated(EnumType.STRING)
    private Role role;

    // Shelves (Many-to-Many with Books)
    @ManyToMany
    @JoinTable(
            name = "users_read",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id")
    )
    private List<Book> read = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "users_currently_reading",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id")
    )
    private List<Book> currentlyReading = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "users_want_to_read",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id")
    )
    private List<Book> wantToRead = new ArrayList<>();

    // Books Published by This User (Only for Authors)
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<Book> myPublishedBooks = new ArrayList<>();


    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    // add role, first name, last name
    public User(String username, String email, String password, Role role, String firstName, String lastName) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public User() {
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name())); // Adds "ROLE_" prefix to the role
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

//    @Override
//    public String getPassword() {
//        return password;
//    }

}
