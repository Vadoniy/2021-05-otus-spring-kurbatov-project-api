package ru.otus.yardsportsteamlobby.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "business.default")
@Component
@Getter
@Setter
public class BusinessConfiguration {

    private String teamNameA;

    private String teamNameB;

    private int capacity;
}
