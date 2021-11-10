package ru.otus.yardsportsteamlobby.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.yardsportsteamlobby.domain.Team;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
