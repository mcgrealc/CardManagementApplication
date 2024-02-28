package org.cmg.sandbox.creditcardapp.repository;

import java.util.Optional;

import org.cmg.sandbox.creditcardapp.model.Role;
import org.cmg.sandbox.creditcardapp.model.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
	
	Optional<Role> findByRole(RoleEnum role);

}
