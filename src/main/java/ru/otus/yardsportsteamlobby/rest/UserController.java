package ru.otus.yardsportsteamlobby.rest;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    @HystrixCommand(commandKey = "userControllerTimeout", defaultFallback = "defaultRole")
    public ResponseEntity<String> getUsersRole(@PathVariable long userId) {
        return new ResponseEntity<>(customUserDetailsService.loadUsersRole(userId), HttpStatus.OK);
    }

    private ResponseEntity<String> defaultRole() {
        log.info("Hystrix default response defaultRole");
        return new ResponseEntity<>(PlayerAuthority.NEW.name(), HttpStatus.OK);
    }
}
