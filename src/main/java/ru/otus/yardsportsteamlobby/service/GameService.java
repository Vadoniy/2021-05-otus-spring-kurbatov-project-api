package ru.otus.yardsportsteamlobby.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import ru.otus.yardsportsteamlobby.configuration.BusinessConfiguration;
import ru.otus.yardsportsteamlobby.domain.Game;
import ru.otus.yardsportsteamlobby.domain.Team;
import ru.otus.yardsportsteamlobby.dto.GameDto;
import ru.otus.yardsportsteamlobby.enums.GameStatus;
import ru.otus.yardsportsteamlobby.repository.GameRepository;
import ru.otus.yardsportsteamlobby.repository.PlayerRepository;
import ru.otus.yardsportsteamlobby.repository.TeamRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class GameService {

    private final BusinessConfiguration businessConfiguration;

    private final GameRepository gameRepository;

    private final PlayerRepository playerRepository;

    private final TeamRepository teamRepository;

    private GameDto cachedGameDto = new GameDto();

    public Game saveGame(Game game) {
        return gameRepository.save(game);
    }

    @Transactional
    public List<GameDto> gameList(int howManyGames) {
        final var pageRequest = PageRequest.of(0, howManyGames, Sort.Direction.ASC, "gameDateTime");
        return gameRepository.findAllByStatus(GameStatus.EXPECTED, pageRequest).stream()
                .map(GameDto::toDto)
                .collect(Collectors.toList());
    }

    public GameDto signUpForGame(long gameId, long teamId, long userId) {
        final var selectedGame = gameRepository.findById(gameId);
        cachedGameDto = selectedGame.map(GameDto::toDto).orElse(new GameDto());
        final var selectedTeam = teamRepository.findById(teamId);
        final var selectedPlayer = Optional.ofNullable(playerRepository.findOneByUserId(userId))
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.MULTI_STATUS));
        final var selectedGameCapacity = selectedGame.map(Game::getTeamCapacity).orElse(businessConfiguration.getCapacity());
        final var otherTeam = selectOtherTeamFromGame(selectedGame.orElseThrow(), teamId);
        if (selectedPlayer == null) {
            return cachedGameDto;
        }
        if (selectedTeam.map(Team::getLineUp).map(Set::size).orElse(0) >= selectedGameCapacity
                && selectedTeam.map(Team::getLineUp).map(players -> !players.contains(selectedPlayer)).orElse(Boolean.TRUE)) {
            if (otherTeam.getLineUp().size() <= selectedGameCapacity) {
                throw new HttpClientErrorException(HttpStatus.ALREADY_REPORTED);
            }
            throw new HttpClientErrorException(HttpStatus.NO_CONTENT);
        } else {
            selectedGame.map(game -> {
                if (game.getTeamA().getId() == teamId) {
                    return game.getTeamA();
                } else {
                    return game.getTeamB();
                }
            })
                    .map(Team::getLineUp)
                    .ifPresent(players -> players.add(selectedPlayer));
            otherTeam.getLineUp().remove(selectedPlayer);
        }
        return selectedGame.map(gameRepository::save)
                .map(GameDto::toDto)
                .orElse(cachedGameDto);
    }

    private Team selectOtherTeamFromGame(Game game, long selectedTeamId) {
        if (game.getTeamA().getId() == selectedTeamId) {
            return game.getTeamB();
        } else {
            return game.getTeamA();
        }
    }
}
