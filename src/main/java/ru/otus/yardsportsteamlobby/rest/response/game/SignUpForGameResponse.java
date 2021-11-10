package ru.otus.yardsportsteamlobby.rest.response.game;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.otus.yardsportsteamlobby.dto.GameDto;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpForGameResponse {

    private Integer errorCode;

    private GameDto gameDto;
}
