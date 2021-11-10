package ru.otus.yardsportsteamlobby.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.otus.yardsportsteamlobby.configuration.BusinessConfiguration;
import ru.otus.yardsportsteamlobby.domain.Game;
import ru.otus.yardsportsteamlobby.domain.Team;
import ru.otus.yardsportsteamlobby.dto.GameDto;
import ru.otus.yardsportsteamlobby.repository.GameRepository;
import ru.otus.yardsportsteamlobby.repository.PlayerRepository;
import ru.otus.yardsportsteamlobby.repository.TeamRepository;
import ru.otus.yardsportsteamlobby.rest.response.game.ListGameResponse;
import ru.otus.yardsportsteamlobby.rest.response.game.SignUpForGameResponse;

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

    public SignUpForGameResponse signUpForGame(long gameId, long teamId, long userId) {
        final var selectedGame = gameRepository.findById(gameId);
        final var selectedTeam = teamRepository.findById(teamId);
        final var selectedPlayer = playerRepository.findOneByUserId(userId);
        final var selectedGameCapacity = selectedGame.map(Game::getTeamCapacity).orElse(businessConfiguration.getCapacity());
        final var signUpForGameResponse = new SignUpForGameResponse();
        final int errorCode;
        if (selectedTeam.map(Team::getLineUp).map(Set::size).orElse(0) >= selectedGameCapacity) {
            errorCode = 2;
            //TODO select other team
        } else {
            selectedGame.map(game -> {
                if (game.getTeamA().getId() == teamId) {
                    return game.getTeamA();
                } else {
                    return game.getTeamB();
                }
            })
                    .map(Team::getLineUp)
                    .ifPresent(players -> {
                        players.add(selectedPlayer);
                    });
            errorCode = 1;
        }
        final var savedGame = selectedGame.map(gameRepository::save);
        savedGame.map(GameDto::toDto)
                .ifPresentOrElse(signUpForGameResponse::setGameDto, () ->
                        signUpForGameResponse.setErrorCode(errorCode));
        return signUpForGameResponse;
    }

    private Team selectAnotherTeamFromGame(Game game, long teamId) {
        if (game.getTeamA().getId() == teamId) {
            return game.getTeamB();
        } else {
            return game.getTeamA();
        }
    }
}
