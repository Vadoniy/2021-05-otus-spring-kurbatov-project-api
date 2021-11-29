package ru.otus.yardsportsteamlobby.service.hystrix;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.yardsportsteamlobby.enums.PlayerAuthority;
import ru.otus.yardsportsteamlobby.service.MyUserService;

@Service
@RequiredArgsConstructor
@Slf4j
public class HystrixMyUserService {

    private final MyUserService myUserService;

    @HystrixCommand(commandKey = "userControllerTimeout", fallbackMethod = "defaultRole")
    public String loadUsersRole(Long userId) {
        return myUserService.loadUsersRole(userId);
    }

    private String defaultRole(Long userId) {
        log.info("Hystrix default response for userId {}, returned rol is NEW", userId);
        return PlayerAuthority.NEW.name();
    }
}
