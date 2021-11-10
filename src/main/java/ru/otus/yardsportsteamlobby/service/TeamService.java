package ru.otus.yardsportsteamlobby.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.yardsportsteamlobby.repository.TeamRepository;

@Service
@RequiredArgsConstructor
public class TeamService {

    private TeamRepository teamRepository;

}
