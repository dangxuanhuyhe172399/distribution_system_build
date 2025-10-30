package com.sep490.bads.distributionsystem.security.otp;

import lombok.Data;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class InMemoryOtpStore {
    private static final long TTL_SECONDS = 300; // 5 phút
    private final Map<Long, Otp> store = new ConcurrentHashMap<>();
    private final Random rnd = new Random();

    public String issue(Long userId) {
        String code = String.format("%06d", rnd.nextInt(1_000_000));
        store.put(userId, new Otp(code, Instant.now().plusSeconds(TTL_SECONDS)));
        return code;
    }

    public boolean validate(Long userId, String code) {
        Otp otp = store.get(userId);
        if (otp == null) return false;
        if (Instant.now().isAfter(otp.expiresAt)) return false;
        return otp.code.equals(code);
    }

    public void clear(Long userId) { store.remove(userId); }

    @Scheduled(fixedRate = 60_000)
    public void sweep() {
        Instant now = Instant.now();
        store.entrySet().removeIf(e -> now.isAfter(e.getValue().expiresAt));
    }

    @Data
    private static class Otp {
        private final String code;
        private final Instant expiresAt;
    }
}
