package ru.otus.yardsportsteamlobby.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.yardsportsteamlobby.domain.Game;

public interface GameRepository extends JpaRepository<Game, Long> {
}
