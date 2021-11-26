package ru.otus.yardsportsteamlobby.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.yardsportsteamlobby.enums.PlayerAuthority;
import ru.otus.yardsportsteamlobby.service.security.CustomUserDetailsService;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final CustomUserDetailsService customUserDetailsService;

    @GetMapping("/user/role/{userId}")
    public String getUsersRole(@PathVariable long userId) {
        try {
            return customUserDetailsService.loadUsersRole(userId);
        } catch (UsernameNotFoundException ex) {
            return PlayerAuthority.NEW.name();
        }
    }
}
