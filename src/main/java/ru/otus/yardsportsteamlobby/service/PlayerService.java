package ru.otus.yardsportsteamlobby.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.yardsportsteamlobby.domain.CustomUser;
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
    public Player savePlayer(Player player) {
        if (playerRepository.existsByUserId(player.getUserId())) {
            player = playerRepository.findOneByUserId(player.getUserId())
                    .setName(player.getName())
                    .setPhone(player.getPhone())
                    .setPlayerNumber(player.getPlayerNumber())
                    .setPosition(player.getPosition());
        }
        player = playerRepository.save(player);
        saveUser(player.getUserId());
        return player;
    }

    @Transactional
    public String deletePlayer(Long userId) {
        if (playerRepository.existsByUserId(userId)) {
            playerRepository.deleteByUserId(userId);
            log.info("Игрок с userId " + userId + " был удалён.");
        } else {
            log.info("Игрок с userId " + userId + " не найден в списке игроков.");
        }
        return PlayerAuthority.NEW.name();
    }

    private CustomUser saveUser(Long userId) {
        if (userRepository.existsByUserId(userId)) {
            return userRepository.findByUserId(userId)
                    .orElseThrow();
        } else {
            return userRepository.save(new CustomUser()
                    .setUserId(userId)
                    .setPassword(String.valueOf(userId).toCharArray())
                    .setAuthority(PlayerAuthority.USER));
        }
    }
}
