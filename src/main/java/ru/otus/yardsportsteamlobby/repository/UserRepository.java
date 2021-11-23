package ru.otus.yardsportsteamlobby.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.yardsportsteamlobby.domain.MyUser;

import java.util.Optional;

public interface UserRepository extends JpaRepository<MyUser, Long> {

    Optional<MyUser> findByUserId(Long userId);

    boolean existsByUserId(Long userId);
}
