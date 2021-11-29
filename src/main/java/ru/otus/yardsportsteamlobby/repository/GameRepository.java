package ru.otus.yardsportsteamlobby.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.yardsportsteamlobby.domain.Game;
import ru.otus.yardsportsteamlobby.enums.GameStatus;

public interface GameRepository extends JpaRepository<Game, Long> {

    Page<Game> findAllByStatus(GameStatus status, Pageable pageable);
}
