package ru.otus.yardsportsteamlobby.rest;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.otus.yardsportsteamlobby.domain.Player;
import ru.otus.yardsportsteamlobby.repository.UserRepository;
import ru.otus.yardsportsteamlobby.rest.request.player.CreatePlayerRequest;
import ru.otus.yardsportsteamlobby.service.PlayerService;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PlayerRestController {

    private final PlayerService playerService;

    private final UserRepository userRepository;

    @PostMapping("/player/new")
    @HystrixCommand(commandKey = "playerServiceTimeout", defaultFallback = "notRegistered")
    public ResponseEntity<String> registerPlayer(@Validated @RequestBody CreatePlayerRequest createPlayerRequest) {
        final var player = new Player()
                .setUserId(createPlayerRequest.getUserId())
                .setName(createPlayerRequest.getName())
                .setPhone(createPlayerRequest.getPhone())
                .setPlayerNumber(createPlayerRequest.getNumber())
                .setPosition(createPlayerRequest.getPosition());
        final var savedPlayer = playerService.savePlayer(player);
        final var response = "Player " + savedPlayer.getName() + " is registered with id " + savedPlayer.getUserId();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/player/{userId}")
    @HystrixCommand(commandKey = "playerServiceTimeout", defaultFallback = "notDeleted")
    public ResponseEntity<String> deletePlayer(@PathVariable String userId) {
        final var response = playerService.deletePlayer(Long.parseLong(userId));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private ResponseEntity<String> notRegistered() {
        log.info("Hystrix default response notRegistered");
        final var response = "Player was not registered, try again later.";
        return new ResponseEntity<>(response, HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS);
    }

    private ResponseEntity<String> notDeleted() {
        log.info("Hystrix default response notDeleted");
        final var response = "Player was not deleted, try again later.";
        return new ResponseEntity<>(response, HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS);
    }
}
