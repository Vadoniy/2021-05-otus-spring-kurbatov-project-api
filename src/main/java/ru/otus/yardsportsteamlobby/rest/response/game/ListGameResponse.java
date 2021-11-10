package ru.otus.yardsportsteamlobby.rest.response.game;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.otus.yardsportsteamlobby.dto.GameDto;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ListGameResponse {

    private List<GameDto> games;
}