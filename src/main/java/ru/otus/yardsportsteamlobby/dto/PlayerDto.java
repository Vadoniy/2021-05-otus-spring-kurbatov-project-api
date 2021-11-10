package ru.otus.yardsportsteamlobby.dto;

import lombok.*;
import ru.otus.yardsportsteamlobby.domain.Player;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlayerDto {

    private Long playerId;

    private String playerName;

    public static PlayerDto toDto(Player player) {
        return new PlayerDto(player.getId(), player.getName());
    }
}
