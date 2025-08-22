package io.syss.auth.config;

import io.syss.auth.model.User;
import io.syss.auth.repo.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataLoader {
    @Bean
    CommandLineRunner seedUsers(UserRepository repo, PasswordEncoder encoder) {
        return args -> {
            if (repo.count() > 0) return;
            repo.save(user("admin", "ADMIN", encoder));
            repo.save(user("commander", "COMMANDER", encoder));
            repo.save(user("analyst", "ANALYST", encoder));
            repo.save(user("field", "FIELD_UNIT", encoder));
            repo.save(user("observer", "OBSERVER", encoder));
        };
    }

    private User user(String username, String role, PasswordEncoder encoder) {
        User u = new User();
        u.setUsername(username);
        u.setPasswordHash(encoder.encode("password"));
        u.setRole(role);
        u.setTwoFactorEnabled(false);
        return u;
    }
}