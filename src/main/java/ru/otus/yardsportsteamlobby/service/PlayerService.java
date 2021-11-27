package ru.otus.yardsportsteamlobby.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.otus.yardsportsteamlobby.domain.MyUser;
import ru.otus.yardsportsteamlobby.domain.Player;
import ru.otus.yardsportsteamlobby.enums.PlayerAuthority;
import ru.otus.yardsportsteamlobby.repository.PlayerRepository;
import ru.otus.yardsportsteamlobby.repository.UserRepository;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlayerService {

    private final PlayerRepository playerRepository;

    private final UserRepository userRepository;

    @Transactional
    @HystrixCommand(commandKey = "playerServiceTimeout", defaultFallback = "notRegistered")
    public Player savePlayer(Player player) {
        final Player p;
        if (playerRepository.existsByUserId(player.getUserId())) {
            p = playerRepository.findOneByUserId(player.getUserId())
                    .setName(player.getName())
                    .setPhone(player.getPhone())
                    .setPlayerNumber(player.getPlayerNumber())
                    .setPosition(player.getPosition());
        } else {
            p = playerRepository.save(player);
        }
        saveUser(p.getUserId());
        return p;
    }

    @Transactional
    @HystrixCommand(commandKey = "playerServiceTimeout", defaultFallback = "notDeleted")
    public String deletePlayer(Long userId) {
        if (playerRepository.existsByUserId(userId)) {
            playerRepository.deleteByUserId(userId);
            return "Игрок с userId " + userId + " был удалён.";
        } else {
            return "Игрок с userId " + userId + " не найден в списке игроков.";
        }
    }

    private MyUser saveUser(Long userId) {
        if (userRepository.existsByUserId(userId)) {
            return userRepository.findByUserId(userId)
                    .orElseThrow();
        } else {
            return userRepository.save(new MyUser()
                    .setUserId(userId)
                    .setPassword(String.valueOf(userId).toCharArray())
                    .setRole(PlayerAuthority.USER));
        }
    }

    private ResponseEntity<String> notDeleted() {
        log.info("Hystrix default response notDeleted");
        final var response = "Player was not deleted, try again later.";
        return new ResponseEntity<>(response, HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS);
    }

    private ResponseEntity<String> notRegistered() {
        log.info("Hystrix default response notRegistered");
        final var response = "Player was not registered, try again later.";
        return new ResponseEntity<>(response, HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS);
    }
}
