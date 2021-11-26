package ru.otus.yardsportsteamlobby.rest;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.otus.yardsportsteamlobby.configuration.BusinessConfiguration;
import ru.otus.yardsportsteamlobby.domain.Game;
import ru.otus.yardsportsteamlobby.domain.Team;
import ru.otus.yardsportsteamlobby.dto.GameDto;
import ru.otus.yardsportsteamlobby.rest.request.game.CreateGameRequest;
import ru.otus.yardsportsteamlobby.rest.response.game.ListGameResponse;
import ru.otus.yardsportsteamlobby.service.GameService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@RestController
@RequiredArgsConstructor
@Slf4j
public class GameRestController {

    private final BusinessConfiguration businessConfiguration;

    private final GameService gameService;

    private List<GameDto> cachedGameList = new ArrayList<>(0);

    private GameDto cachedGameDto = new GameDto();

    @PostMapping("/game/new")
    @HystrixCommand(commandKey = "gameServiceTimeout", defaultFallback = "gameNotCreated")
    public ResponseEntity<String> createGame(@RequestBody CreateGameRequest createGameRequest) throws InterruptedException {
        final var tt = new Random().nextInt(10);
        if (tt%2==0) {
            log.info("COUNTER {}", new Random().nextInt(10));
            Thread.sleep(1500);
        }
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
        final var response = "Game is created! Date " + gameSaved.getGameDateTime().toLocalDate() + ", time " + gameSaved.getGameDateTime().toLocalTime()
                + ", teams: " + gameSaved.getTeamA().getTeamName() + " - " + gameSaved.getTeamB().getTeamName();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/game/list/{amountOfGames}")
    @HystrixCommand(commandKey = "gameServiceTimeout", defaultFallback = "cachedGamesList")
    public ListGameResponse getGameList(@PathVariable int amountOfGames) {
        cachedGameList = Optional.ofNullable(gameService.gameList(amountOfGames))
                .map(ListGameResponse::getGames)
                .orElse(new ArrayList<>(0));
        return gameService.gameList(amountOfGames);
    }

    @PostMapping("/game/{gameId}/team/{teamId}/player/{userId}")
    @HystrixCommand(commandKey = "gameServiceTimeout", defaultFallback = "cachedGameDto")
    public ResponseEntity<GameDto> signUpForGame(@PathVariable long gameId, @PathVariable long teamId, @PathVariable long userId) {
        return gameService.signUpForGame(gameId, teamId, userId);
    }

    private ResponseEntity<String> gameNotCreated() {
        final var response = "Game was not created, try again later.";
        return new ResponseEntity<>(response, HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS);
    }

    private ListGameResponse cachedGamesList() {
        final var response = new ListGameResponse();
        response.setGames(cachedGameList);
        return response;
    }

    private ResponseEntity<GameDto> cachedGameDto() {
        return new ResponseEntity<>(cachedGameDto, HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS);
    }
}
