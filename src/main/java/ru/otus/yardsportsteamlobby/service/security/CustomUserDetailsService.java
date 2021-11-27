package ru.otus.yardsportsteamlobby.service.security;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.otus.yardsportsteamlobby.domain.MyUser;
import ru.otus.yardsportsteamlobby.enums.PlayerAuthority;
import ru.otus.yardsportsteamlobby.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        return userRepository.findByUserId(Long.parseLong(userId))
                .map(u -> new User(
                        String.valueOf(u.getUserId()), new BCryptPasswordEncoder().encode(String.valueOf(u.getUserId())),
                        AuthorityUtils.createAuthorityList(u.getRole().name())))
                .orElse(new User(userId, passwordEncoder.encode(userId),
                        AuthorityUtils.createAuthorityList(PlayerAuthority.NEW.name())));
    }

    @HystrixCommand(commandKey = "userControllerTimeout", defaultFallback = "defaultRole")
    public String loadUsersRole(Long userId) {
        return userRepository.findByUserId(userId)
                .map(MyUser::getRole)
                .map(PlayerAuthority::name)
                .orElseThrow(() -> new UsernameNotFoundException("User with userId " + userId + " is not presented in database."));
    }

    private String defaultRole() {
        log.info("Hystrix default response defaultRole");
        return PlayerAuthority.NEW.name();
    }
}