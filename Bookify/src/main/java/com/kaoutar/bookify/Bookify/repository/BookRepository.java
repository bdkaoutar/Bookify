package com.kaoutar.bookify.Bookify.repository;

import com.kaoutar.bookify.Bookify.model.Book;
import com.kaoutar.bookify.Bookify.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends CrudRepository<Book, Long> {
    Optional<Book> findByTitle(String title);
    Optional<Book> findByAuthor(User author);
    List<Book> findByGenres(String genre);
    Optional<Book> findByLanguage(String language);
    List<Book> findTop9ByOrderByCreatedAtDesc();

}
