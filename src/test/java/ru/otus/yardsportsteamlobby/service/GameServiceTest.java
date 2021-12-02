package ru.otus.yardsportsteamlobby.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import ru.otus.yardsportsteamlobby.domain.Game;
import ru.otus.yardsportsteamlobby.domain.Player;
import ru.otus.yardsportsteamlobby.domain.Team;
import ru.otus.yardsportsteamlobby.dto.GameDto;
import ru.otus.yardsportsteamlobby.enums.GameStatus;
import ru.otus.yardsportsteamlobby.enums.PlayerPosition;
import ru.otus.yardsportsteamlobby.enums.PlayerState;
import ru.otus.yardsportsteamlobby.repository.GameRepository;
import ru.otus.yardsportsteamlobby.repository.PlayerRepository;
import ru.otus.yardsportsteamlobby.repository.TeamRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class GameServiceTest {

    @Autowired
    private GameService gameService;

    @MockBean
    private GameRepository gameRepository;

    @MockBean
    private PlayerRepository playerRepository;

    @MockBean
    private TeamRepository teamRepository;

    @MockBean
    private GameDto gameDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void gameListTest() {
        final var random = new Random();
        final var playerA = new Player()
                .setPlayerNumber(random.nextInt(99))
                .setName(RandomStringUtils.random(10))
                .setPhone(RandomStringUtils.randomAlphanumeric(11))
                .setPosition(PlayerPosition.FIELD)
                .setUserId(new Random().nextLong())
                .setState(PlayerState.ACTIVE);
        final var playerB = new Player()
                .setPlayerNumber(random.nextInt(99))
                .setName(RandomStringUtils.random(10))
                .setPhone(RandomStringUtils.randomAlphanumeric(11))
                .setPosition(PlayerPosition.UNIQUE)
                .setUserId(new Random().nextLong())
                .setState(PlayerState.ACTIVE);
        final var teamA = new Team()
                .setTeamName(RandomStringUtils.random(10))
                .setLineUp(Set.of(playerA))
                .setId(random.nextLong());
        final var teamB = new Team()
                .setTeamName(RandomStringUtils.random(10))
                .setLineUp(Set.of(playerB))
                .setId(random.nextLong());
        final var game = new Game()
                .setGameDateTime(LocalDateTime.now().plusDays(5))
                .setStatus(GameStatus.EXPECTED)
                .setTeamA(teamA)
                .setTeamB(teamB)
                .setTeamCapacity(new Random().nextInt(9) + 1);
        when(gameRepository.findAllByStatus(any(), any())).thenReturn(new PageImpl<>(List.of(game)));
        final var howManyGames = 1;
        final var gameList = gameService.gameList(howManyGames);
        Assertions.assertEquals(1, gameList.size());
    }
}