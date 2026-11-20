package com.estateease.repository;
import com.estateease.model.AppUser; import java.util.Optional; import org.springframework.data.jpa.repository.JpaRepository;
public interface UserRepository extends JpaRepository<AppUser,Long>{ Optional<AppUser> findByEmail(String email); boolean existsByEmail(String email); }
