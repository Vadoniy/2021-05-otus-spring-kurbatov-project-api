package ru.otus.yardsportsteamlobby.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.yardsportsteamlobby.domain.MyUser;
import ru.otus.yardsportsteamlobby.domain.Player;
import ru.otus.yardsportsteamlobby.enums.PlayerRole;
import ru.otus.yardsportsteamlobby.repository.PlayerRepository;
import ru.otus.yardsportsteamlobby.repository.UserRepository;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class PlayerService {

    private final PlayerRepository playerRepository;

    private final UserRepository userRepository;

    @Transactional
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
                    .setRole(PlayerRole.USER));
        }
    }
}
