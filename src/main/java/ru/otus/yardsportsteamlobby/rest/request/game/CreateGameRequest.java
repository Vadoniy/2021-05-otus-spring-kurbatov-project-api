package ru.otus.yardsportsteamlobby.rest.request.game;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateGameRequest {

    public static final String GAME_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm";

    @JsonFormat(pattern = GAME_DATE_TIME_FORMAT)
    private LocalDateTime gameDateTime;

    private Integer teamCapacity;

    private String teamNameA;

    private String teamNameB;
}
