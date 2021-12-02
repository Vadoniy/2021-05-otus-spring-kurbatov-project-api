package ru.otus.yardsportsteamlobby.domain;

import lombok.*;
import lombok.experimental.Accessors;
import ru.otus.yardsportsteamlobby.enums.PlayerAuthority;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "userId")
//@EqualsAndHashCode(of = "id")
@Accessors(chain = true)
@Entity
@Table(name = "MY_USER")
public class MyUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    private Long userId;

    @ToString.Exclude
    private char[] password;

    @Enumerated(EnumType.STRING)
    private PlayerAuthority role;
}


