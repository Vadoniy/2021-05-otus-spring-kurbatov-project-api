package ru.otus.yardsportsteamlobby.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.yardsportsteamlobby.domain.Player;
import ru.otus.yardsportsteamlobby.repository.PlayerRepository;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class PlayerService {

    private final PlayerRepository playerRepository;

    @Transactional
    public Player savePlayer(Player player) {
        if (playerRepository.existsByUserId(player.getUserId())) {
            final var p = playerRepository.findOneByUserId(player.getUserId());
            p.setName(player.getName())
                    .setPhone(player.getPhone())
                    .setPlayerNumber(player.getPlayerNumber())
                    .setPosition(player.getPosition());
            return playerRepository.save(p);
        } else {
            return playerRepository.save(player);
        }
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
}
