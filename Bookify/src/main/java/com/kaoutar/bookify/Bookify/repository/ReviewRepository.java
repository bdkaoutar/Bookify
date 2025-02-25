package com.kaoutar.bookify.Bookify.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.kaoutar.bookify.Bookify.model.Review;

import java.util.Optional;

@Repository
public interface ReviewRepository extends CrudRepository<Review, Long> {
    Optional<Review> findByBookId(Long bookId);
    Optional<Review> findByUserId(Long userId);
    Optional<Review> findByRating(int rating);
}
