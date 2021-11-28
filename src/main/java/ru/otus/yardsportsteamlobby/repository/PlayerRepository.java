package ru.otus.yardsportsteamlobby.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.yardsportsteamlobby.domain.Player;

public interface PlayerRepository extends JpaRepository<Player, Long> {

    boolean existsByUserId(Long userId);

    void deleteByUserId(Long userId);

    Player findOneByUserId(Long userId);
}
