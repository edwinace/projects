package org.univ7.webapp.quiz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.univ7.webapp.quiz.entity.PublicPrivateKeyMapper;

public interface PublicPrivateKeyMapperRepository extends JpaRepository<PublicPrivateKeyMapper, Long>{
	PublicPrivateKeyMapper findByPublicKey(String publicKey);
}
