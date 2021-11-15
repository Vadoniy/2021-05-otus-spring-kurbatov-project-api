package ru.otus.yardsportsteamlobby.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.otus.yardsportsteamlobby.configuration.BusinessConfiguration;
import ru.otus.yardsportsteamlobby.domain.Game;
import ru.otus.yardsportsteamlobby.domain.Team;
import ru.otus.yardsportsteamlobby.dto.GameDto;
import ru.otus.yardsportsteamlobby.rest.request.game.CreateGameRequest;
import ru.otus.yardsportsteamlobby.rest.response.game.ListGameResponse;
import ru.otus.yardsportsteamlobby.service.GameService;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
public class GameRestController {

    private final BusinessConfiguration businessConfiguration;

    private final GameService gameService;

    @PostMapping("/game/new")
    public String createGame(@RequestBody CreateGameRequest createGameRequest) {
        final var teamNameA = Optional.ofNullable(createGameRequest.getTeamNameA())
                .orElse(businessConfiguration.getTeamNameA());
        final var teamNameB = Optional.ofNullable(createGameRequest.getTeamNameB())
                .orElse(businessConfiguration.getTeamNameB());
        final var teamA = new Team()
                .setTeamName(teamNameA);
        final var teamB = new Team()
                .setTeamName(teamNameB);
        final var game = new Game()
                .setGameDateTime(createGameRequest.getGameDateTime())
                .setTeamCapacity(createGameRequest.getTeamCapacity())
                .setTeamA(teamA)
                .setTeamB(teamB);
        final var gameSaved = gameService.saveGame(game);
        return "Game is created! Date " + gameSaved.getGameDateTime().toLocalDate() + ", time " + gameSaved.getGameDateTime().toLocalTime()
                + ", teams: " + gameSaved.getTeamA().getTeamName() + " - " + gameSaved.getTeamB().getTeamName();
    }

    @GetMapping("/game/list/{amountOfGames}")
    public ListGameResponse getGameList(@PathVariable int amountOfGames) {
        return gameService.gameList(amountOfGames);
    }

    @PostMapping("/game/{gameId}/team/{teamId}/player/{userId}")
    public ResponseEntity<GameDto> signUpForGame(@PathVariable long gameId, @PathVariable long teamId, @PathVariable long userId) {
        return gameService.signUpForGame(gameId, teamId, userId);
    }
}
