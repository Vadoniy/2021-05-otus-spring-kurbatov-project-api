package ru.otus.yardsportsteamlobby.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import ru.otus.yardsportsteamlobby.enums.GameStatus;
import ru.otus.yardsportsteamlobby.enums.PlayerPosition;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Accessors(chain = true)
@Entity
@Table(name = "GAME")
public class Game {

    public static final String GAME_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    private Integer teamCapacity;

    @Enumerated(EnumType.STRING)
    private GameStatus status = GameStatus.EXPECTED;

    @NotNull
    @JsonFormat(pattern = GAME_DATE_TIME_FORMAT)
    private LocalDateTime gameDateTime;

    @OneToOne(targetEntity = Team.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "TEAM_A_ID")
    private Team teamA = new Team();

    @OneToOne(targetEntity = Team.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "TEAM_B_ID")
    private Team teamB = new Team();
}
