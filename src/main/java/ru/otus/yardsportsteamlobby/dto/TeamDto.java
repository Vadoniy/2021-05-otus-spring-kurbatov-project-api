package ru.otus.yardsportsteamlobby.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.otus.yardsportsteamlobby.domain.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TeamDto {

    private Long teamId;

    private String teamName;

    private List<PlayerDto> lineUp = new ArrayList<>();

    public static TeamDto toDto(Team team) {
        final var lineUp = team.getLineUp().stream()
                .map(PlayerDto::toDto)
                .collect(Collectors.toList());
        return new TeamDto(team.getId(), team.getTeamName(), lineUp);
    }
}
