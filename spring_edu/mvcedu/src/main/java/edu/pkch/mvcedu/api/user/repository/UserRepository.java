package edu.pkch.mvcedu.api.user.repository;

import edu.pkch.mvcedu.api.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
