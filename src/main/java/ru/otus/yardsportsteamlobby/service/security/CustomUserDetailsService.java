package ru.otus.yardsportsteamlobby.service.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.otus.yardsportsteamlobby.domain.MyUser;
import ru.otus.yardsportsteamlobby.enums.PlayerRole;
import ru.otus.yardsportsteamlobby.repository.UserRepository;

@Service
@RequiredArgsConstructor
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
                        AuthorityUtils.createAuthorityList(PlayerRole.NEW.name())));
    }

    public String loadUsersRole(Long userId) {
        return userRepository.findByUserId(userId)
                .map(MyUser::getRole)
                .map(PlayerRole::name)
                .orElse(PlayerRole.NEW.name());
    }
}