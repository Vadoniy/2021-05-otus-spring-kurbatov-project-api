package ru.otus.yardsportsteamlobby.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.otus.yardsportsteamlobby.domain.Player;
import ru.otus.yardsportsteamlobby.rest.request.player.CreatePlayerRequest;
import ru.otus.yardsportsteamlobby.service.hystrix.HystrixCustomUserService;
import ru.otus.yardsportsteamlobby.service.hystrix.HystrixPlayerService;

@RestController
@RequiredArgsConstructor
public class PlayerRestController {

    private final HystrixPlayerService hystrixPlayerService;

    private final HystrixCustomUserService hystrixCustomUserService;

    @PostMapping("/player/new")
    public ResponseEntity<String> registerPlayer(@Validated @RequestBody CreatePlayerRequest createPlayerRequest) {
        final var player = new Player()
                .setUserId(createPlayerRequest.getUserId())
                .setName(createPlayerRequest.getName())
                .setPhone(createPlayerRequest.getPhone())
                .setPlayerNumber(createPlayerRequest.getNumber())
                .setPosition(createPlayerRequest.getPosition());
        final var savedPlayer = hystrixPlayerService.savePlayer(player);
        final var response = hystrixCustomUserService.loadUsersRole(savedPlayer.getUserId());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/player/{userId}")
    public ResponseEntity<String> deletePlayer(@PathVariable String userId) {
        final var response = hystrixPlayerService.deletePlayer(Long.parseLong(userId));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
