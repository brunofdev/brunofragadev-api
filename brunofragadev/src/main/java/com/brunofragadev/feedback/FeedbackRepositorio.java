package com.brunofragadev.feedback;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackRepositorio extends JpaRepository<Feedback, Long> {
}
