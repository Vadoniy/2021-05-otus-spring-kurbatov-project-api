package ru.otus.yardsportsteamlobby.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.yardsportsteamlobby.service.hystrix.HystrixCustomUserService;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final HystrixCustomUserService hystrixCustomUserService;

    @GetMapping("/user/role/{userId}")
    public ResponseEntity<String> getUsersRole(@PathVariable long userId) {
        return new ResponseEntity<>(hystrixCustomUserService.loadUsersRole(userId), HttpStatus.OK);
    }
}
