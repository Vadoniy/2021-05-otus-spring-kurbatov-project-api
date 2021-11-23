package ru.otus.yardsportsteamlobby.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.otus.yardsportsteamlobby.domain.MyUser;
import ru.otus.yardsportsteamlobby.domain.Player;
import ru.otus.yardsportsteamlobby.enums.PlayerRole;
import ru.otus.yardsportsteamlobby.repository.UserRepository;
import ru.otus.yardsportsteamlobby.rest.request.player.CreatePlayerRequest;
import ru.otus.yardsportsteamlobby.service.PlayerService;

@RestController
@RequiredArgsConstructor
public class PlayerRestController {

    private final PlayerService playerService;

    private final UserRepository userRepository;

    @PostMapping("/player/new")
    public String registerPlayer(@Validated @RequestBody CreatePlayerRequest createPlayerRequest) {
        final var player = new Player()
                .setUserId(createPlayerRequest.getUserId())
                .setName(createPlayerRequest.getName())
                .setPhone(createPlayerRequest.getPhone())
                .setPlayerNumber(createPlayerRequest.getNumber())
                .setPosition(createPlayerRequest.getPosition());
        final var savedPlayer = playerService.savePlayer(player);
        userRepository.save(new MyUser()
                .setUserId(createPlayerRequest.getUserId())
                .setPassword(String.valueOf(createPlayerRequest.getUserId()).toCharArray())
                .setRole(PlayerRole.USER));
        return "Player " + savedPlayer.getName() + " is registered with id " + savedPlayer.getUserId();
    }

    @DeleteMapping("/player/{userId}")
    public String deletePlayer(@PathVariable String userId) {
        return playerService.deletePlayer(Long.parseLong(userId));
    }
}
