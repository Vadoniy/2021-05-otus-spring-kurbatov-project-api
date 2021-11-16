package ru.otus.yardsportsteamlobby.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.otus.yardsportsteamlobby.configuration.BusinessConfiguration;
import ru.otus.yardsportsteamlobby.domain.Game;
import ru.otus.yardsportsteamlobby.domain.Team;
import ru.otus.yardsportsteamlobby.dto.GameDto;
import ru.otus.yardsportsteamlobby.repository.GameRepository;
import ru.otus.yardsportsteamlobby.repository.PlayerRepository;
import ru.otus.yardsportsteamlobby.repository.TeamRepository;
import ru.otus.yardsportsteamlobby.rest.response.game.ListGameResponse;

import javax.transaction.Transactional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GameService {

    private final BusinessConfiguration businessConfiguration;

    private final GameRepository gameRepository;

    private final PlayerRepository playerRepository;

    private final TeamRepository teamRepository;

    @Transactional
    public Game saveGame(Game game) {
        return gameRepository.save(game);
    }

    public ListGameResponse gameList(int howManyGames) {
        final var pageRequest = PageRequest.of(0, howManyGames, Sort.Direction.ASC, "gameDateTime");
        final var lastGames = gameRepository.findAll(pageRequest).stream()
                .map(GameDto::toDto)
                .collect(Collectors.toList());
        return new ListGameResponse(lastGames);
    }

    public ResponseEntity<GameDto> signUpForGame(long gameId, long teamId, long userId) {
        final var selectedGame = gameRepository.findById(gameId);
        final var selectedTeam = teamRepository.findById(teamId);
        final var selectedPlayer = playerRepository.findOneByUserId(userId);
        final var selectedGameCapacity = selectedGame.map(Game::getTeamCapacity).orElse(businessConfiguration.getCapacity());
        final var otherTeam = selectOtherTeamFromGame(selectedGame.orElseThrow(), teamId);
        final var responseGameDto = new GameDto();
        if (selectedPlayer == null) {
            return new ResponseEntity<>(responseGameDto, HttpStatus.MULTI_STATUS);
        }
        if (selectedTeam.map(Team::getLineUp).map(Set::size).orElse(0) >= selectedGameCapacity
                && selectedTeam.map(Team::getLineUp).map(players -> !players.contains(selectedPlayer)).orElse(Boolean.TRUE)) {
            if (otherTeam.getLineUp().size() <= selectedGameCapacity) {
                return new ResponseEntity<>(responseGameDto, HttpStatus.ALREADY_REPORTED);
            }
            return new ResponseEntity<>(responseGameDto, HttpStatus.NO_CONTENT);
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
                .map(dto -> new ResponseEntity<>(dto, HttpStatus.OK))
                .orElse(new ResponseEntity<>(new GameDto(), HttpStatus.NO_CONTENT));
    }

    private Team selectOtherTeamFromGame(Game game, long selectedTeamId) {
        if (game.getTeamA().getId() == selectedTeamId) {
            return game.getTeamB();
        } else {
            return game.getTeamA();
        }
    }
}
