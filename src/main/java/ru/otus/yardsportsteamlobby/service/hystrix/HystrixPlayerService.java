package ru.otus.yardsportsteamlobby.service.hystrix;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.yardsportsteamlobby.domain.Player;
import ru.otus.yardsportsteamlobby.enums.PlayerAuthority;
import ru.otus.yardsportsteamlobby.service.PlayerService;

@Service
@RequiredArgsConstructor
@Slf4j
public class HystrixPlayerService {

    private final PlayerService playerService;

    @HystrixCommand(commandKey = "playerServiceTimeout", fallbackMethod = "notRegistered")
    public Player savePlayer(Player player) {
        return playerService.savePlayer(player);
    }

    @HystrixCommand(commandKey = "playerServiceTimeout", fallbackMethod = "notDeleted")
    public String deletePlayer(Long userId) {
        return playerService.deletePlayer(userId);
    }

    private Player notRegistered(Player player) {
        log.info("Hystrix default response notRegistered for userId {}", player.getUserId());
        return new Player().setRole(PlayerAuthority.NEW).setUserId(player.getUserId());
    }

    private String notDeleted(Long userId) {
        log.info("Hystrix default response notDeleted for userId {}", userId);
        return PlayerAuthority.USER.name();
    }
}
