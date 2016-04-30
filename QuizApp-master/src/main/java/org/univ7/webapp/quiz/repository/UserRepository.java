package org.univ7.webapp.quiz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.univ7.webapp.quiz.entity.User;

public interface UserRepository extends JpaRepository<User, Long>{
	User findByUserId(String userId);
}
