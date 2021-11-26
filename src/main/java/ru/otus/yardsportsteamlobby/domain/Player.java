package ru.otus.yardsportsteamlobby.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import ru.otus.yardsportsteamlobby.enums.PlayerPosition;
import ru.otus.yardsportsteamlobby.enums.PlayerAuthority;
import ru.otus.yardsportsteamlobby.enums.PlayerState;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Accessors(chain = true)
@Entity
@Table(name = "PLAYER")
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    private long userId;

    @NotBlank
    private String name;

    @Size(max = 11)
    @NotBlank
    private String phone;

    private int playerNumber;

    @Enumerated(EnumType.STRING)
    private PlayerPosition position;

    private Boolean isDisqualified = Boolean.FALSE;

    private LocalDate disqualifiedTill;

    @Enumerated(EnumType.STRING)
    private PlayerState state = PlayerState.ACTIVE;

    @Enumerated(EnumType.STRING)
    private PlayerAuthority role = PlayerAuthority.USER;
}

