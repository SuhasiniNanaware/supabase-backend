package gourment_delight_backend.supabase_backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import gourment_delight_backend.supabase_backend.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

}