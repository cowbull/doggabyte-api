package com.doggabyte.repository;

import com.doggabyte.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	@Query("SELECT u FROM User u WHERE u.email = ?1")
    User findByEmail(String email);

	@Query("SELECT u FROM User u WHERE u.name = ?1")
	Optional<User> findByName(String name);

	Boolean existsByName(String name);

	Boolean existsByEmail(String email);
}
