package ru.otus.yardsportsteamlobby.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.otus.yardsportsteamlobby.domain.Game;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GameDto {

    private Long gameId;

    @JsonFormat(pattern = Game.GAME_DATE_TIME_FORMAT)
    private LocalDateTime gameDateTime;

    private int teamCapacity;

    private TeamDto teamA;

    private TeamDto teamB;

    public static GameDto toDto(Game game) {
        final var teamA = TeamDto.toDto(game.getTeamA());
        final var teamB = TeamDto.toDto(game.getTeamB());
        return new GameDto(game.getId(), game.getGameDateTime(), game.getTeamCapacity(), teamA, teamB);
    }
}
