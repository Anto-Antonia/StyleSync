package com.example.StyleSync.Schedule;

import com.example.StyleSync.repository.RevokedTokenRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class SchedulerForCleanup {
    private final RevokedTokenRepository tokenRepository;

    public SchedulerForCleanup(RevokedTokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Scheduled(fixedRate = 3600000)
    public void revokedTokenCleanup(){
        tokenRepository.deleteAllByExpiresAtBefore(LocalDateTime.now());
    }
}
