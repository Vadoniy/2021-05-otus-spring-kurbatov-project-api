package ru.otus.yardsportsteamlobby.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.yardsportsteamlobby.domain.Game;
import ru.otus.yardsportsteamlobby.enums.GameStatus;

import javax.transaction.Transactional;

public interface GameRepository extends JpaRepository<Game, Long> {

    @Transactional
    Page<Game> findAllByStatus(GameStatus status, Pageable pageable);
}
