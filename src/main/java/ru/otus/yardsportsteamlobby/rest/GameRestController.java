package ru.otus.yardsportsteamlobby.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import ru.otus.yardsportsteamlobby.configuration.BusinessConfiguration;
import ru.otus.yardsportsteamlobby.domain.Game;
import ru.otus.yardsportsteamlobby.domain.Team;
import ru.otus.yardsportsteamlobby.dto.GameDto;
import ru.otus.yardsportsteamlobby.enums.GameStatus;
import ru.otus.yardsportsteamlobby.rest.request.game.CreateGameRequest;
import ru.otus.yardsportsteamlobby.rest.response.game.ListGameResponse;
import ru.otus.yardsportsteamlobby.service.GameService;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class GameRestController {

    private final BusinessConfiguration businessConfiguration;

    private final GameService gameService;

    @PostMapping("/game/new")
    public ResponseEntity<String> createGame(@RequestBody CreateGameRequest createGameRequest) {
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
                .setStatus(LocalDateTime.now().isBefore(createGameRequest.getGameDateTime()) ? GameStatus.EXPECTED : GameStatus.PASSED)
                .setTeamCapacity(createGameRequest.getTeamCapacity())
                .setTeamA(teamA)
                .setTeamB(teamB);
        final var gameSaved = gameService.saveGame(createGameRequest.getGameDateTime(), game);
        final var response = "Game is created! Date " + gameSaved.getGameDateTime().toLocalDate() + ", time " + gameSaved.getGameDateTime().toLocalTime()
                + ", teams: " + gameSaved.getTeamA().getTeamName() + " - " + gameSaved.getTeamB().getTeamName();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/game/list/{amountOfGames}")
    public ListGameResponse getGameList(@PathVariable int amountOfGames) {
        return gameService.gameList(amountOfGames);
    }

    @PostMapping("/game/{gameId}/team/{teamId}/player/{userId}")
    public ResponseEntity<GameDto> signUpForGame(@PathVariable long gameId, @PathVariable long teamId, @PathVariable long userId) {
        try {
            return new ResponseEntity<>(gameService.signUpForGame(gameId, teamId, userId), HttpStatus.OK);
        } catch (HttpClientErrorException e) {
            return new ResponseEntity<>(gameService.getCachedGameDto(), e.getStatusCode());
        }
    }
}
