package org.univ7.webapp.quiz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.univ7.webapp.quiz.entity.Question;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}
