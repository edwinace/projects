package org.univ7.webapp.quiz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.univ7.webapp.quiz.entity.UserAuthority;

public interface UserAuthorityRepository extends JpaRepository<UserAuthority, Long>{
	UserAuthority findByUserId(String userId);
}
