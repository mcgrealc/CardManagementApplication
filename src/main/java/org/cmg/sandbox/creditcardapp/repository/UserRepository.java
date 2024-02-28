package org.cmg.sandbox.creditcardapp.repository;

import java.util.Optional;

import org.cmg.sandbox.creditcardapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
	
	Optional<User> findByUserName(String userName);
	
	Boolean existsByUserName(String userName);
	
	Boolean existsByEmail(String email);

}
