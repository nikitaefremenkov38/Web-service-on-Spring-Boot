package ru.mtuci.coursemanagement.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.mtuci.coursemanagement.repository.UserRepository;

@Component
@RequiredArgsConstructor
public class PasswordMigrationRunner implements CommandLineRunner {
    private final UserRepository users;
    private final PasswordEncoder encoder;

    @Override
    public void run(String... args) {
        users.findAll().forEach(u -> {
            String pwd = u.getPassword();
            if (pwd != null && !pwd.startsWith("$2a$") && !pwd.startsWith("$2b$") && !pwd.startsWith("$2y$")) {
                u.setPassword(encoder.encode(pwd));
                users.save(u);
            }
        });
    }
}
