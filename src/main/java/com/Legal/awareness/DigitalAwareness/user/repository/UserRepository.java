package com.Legal.awareness.DigitalAwareness.user.repository;


import com.Legal.awareness.DigitalAwareness.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsernameAndDeletedFalse(String username);
}
