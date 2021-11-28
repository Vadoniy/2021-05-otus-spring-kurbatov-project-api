package ru.otus.yardsportsteamlobby.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.yardsportsteamlobby.service.hystrix.HystrixMyUserService;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final HystrixMyUserService hystrixMyUserService;

    @GetMapping("/user/role/{userId}")
    public ResponseEntity<String> getUsersRole(@PathVariable long userId) {
        return new ResponseEntity<>(hystrixMyUserService.loadUsersRole(userId), HttpStatus.OK);
    }
}
