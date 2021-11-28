package ru.otus.yardsportsteamlobby.service.hystrix;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import ru.otus.yardsportsteamlobby.domain.Game;
import ru.otus.yardsportsteamlobby.dto.GameDto;
import ru.otus.yardsportsteamlobby.service.GameService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class HystrixGameService {

    private final GameService gameService;

    private List<GameDto> cachedGameList = new ArrayList<>(0);

    private GameDto cachedGameDto = new GameDto();

    @HystrixCommand(commandKey = "gameServiceTimeout", fallbackMethod = "gameNotCreated")
    public Game saveGame(LocalDateTime gameDateTime, Game game) {
        return gameService.saveGame(game);
    }

    @HystrixCommand(commandKey = "gameServiceTimeout", fallbackMethod = "cachedGamesList")
    public List<GameDto> gameList(int howManyGames) {
        cachedGameList = gameService.gameList(howManyGames);
        return cachedGameList;
    }

    @HystrixCommand(commandKey = "gameServiceTimeout", fallbackMethod = "cachedGameDto", ignoreExceptions = {HttpClientErrorException.class})
    public GameDto signUpForGame(long gameId, long teamId, long userId) {
        cachedGameDto = gameService.signUpForGame(gameId, teamId, userId);
        return cachedGameDto;
    }

    public GameDto getCachedGameDto() {
        return cachedGameDto;
    }

    private Game gameNotCreated(LocalDateTime gameDateTime, Game game) {
        log.info("Hystrix default response gameNotCreated");
        log.info("Game was not created on {}, try again later.", gameDateTime);
        return new Game();
    }

    private List<GameDto> cachedGamesList(int howManyGames) {
        log.info("Hystrix default response cachedGamesList");
        return cachedGameList;
    }

    private GameDto cachedGameDto(long gameId, long teamId, long userId) {
        log.info("Hystrix default response cachedGameDto: gameId {}, teamId {}, userId {}", gameId, teamId, userId);
        return cachedGameDto;
    }
}
