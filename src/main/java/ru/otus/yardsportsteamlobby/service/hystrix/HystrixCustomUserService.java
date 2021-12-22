package ru.otus.yardsportsteamlobby.service.hystrix;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.yardsportsteamlobby.enums.PlayerAuthority;
import ru.otus.yardsportsteamlobby.service.CustomUserService;

@Service
@RequiredArgsConstructor
@Slf4j
public class HystrixCustomUserService {

    private final CustomUserService customUserService;

    @HystrixCommand(commandKey = "userControllerTimeout", fallbackMethod = "defaultRole")
    public String loadUsersRole(Long userId) {
        return customUserService.loadUsersRole(userId);
    }

    private String defaultRole(Long userId) {
        log.info("Hystrix default response for userId {}, returned rol is NEW", userId);
        return PlayerAuthority.NEW.name();
    }
}
