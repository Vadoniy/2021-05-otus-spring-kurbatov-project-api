package ru.otus.yardsportsteamlobby.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.otus.yardsportsteamlobby.domain.MyUser;
import ru.otus.yardsportsteamlobby.enums.PlayerAuthority;
import ru.otus.yardsportsteamlobby.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class MyUserService {

    private final UserRepository userRepository;

    public String loadUsersRole(Long userId) {
        try {
            return userRepository.findByUserId(userId)
                    .map(MyUser::getRole)
                    .map(PlayerAuthority::name)
                    .orElseThrow(() -> new UsernameNotFoundException("User with userId " + userId + " is not presented in database."));
        } catch (Exception e) {
            log.error(e.getMessage());
            return PlayerAuthority.NEW.name();
        }
    }
}
