package ru.otus.yardsportsteamlobby.rest.request.player;

import lombok.Data;
import ru.otus.yardsportsteamlobby.enums.PlayerPosition;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class CreatePlayerRequest {

    private long userId;

    @NotBlank
    private String name;

    @NotBlank
    @Pattern(regexp = "\\d{10,11}")
    private String phone;

    private PlayerPosition position;

    private int number;
}
