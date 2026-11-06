package br.com.agservpro.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
import java.nio.charset.StandardCharsets;

@Component
public class AdminBootstrap implements ApplicationRunner {
    private static final Logger logger = LoggerFactory.getLogger(AdminBootstrap.class);

    private final JdbcTemplate jdbcTemplate;
    private final PasswordEncoder passwordEncoder;
    private final Environment environment;

    public AdminBootstrap(JdbcTemplate jdbcTemplate, PasswordEncoder passwordEncoder, Environment environment) {
        this.jdbcTemplate = jdbcTemplate;
        this.passwordEncoder = passwordEncoder;
        this.environment = environment;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        String email = environment.getProperty("app.bootstrap.admin-email", "").trim().toLowerCase(Locale.ROOT);
        String password = environment.getProperty("app.bootstrap.admin-password", "");
        String name = environment.getProperty("app.bootstrap.admin-name", "").trim();
        if (name.isBlank()) {
            name = "Administrador SaaS";
        }

        if (email.isBlank() || password.isBlank()) {
            logger.info("Administrador SaaS inicial não configurado; defina ADMIN_SAAS_EMAIL e ADMIN_SAAS_PASSWORD.");
            return;
        }
        if (password.length() < 12 || password.getBytes(StandardCharsets.UTF_8).length > 72) {
            throw new IllegalStateException("ADMIN_SAAS_PASSWORD deve ter pelo menos 12 caracteres e no máximo 72 bytes UTF-8.");
        }

        Integer count = jdbcTemplate.queryForObject(
                "SELECT count(*) FROM usuario WHERE lower(email) = ?", Integer.class, email);
        if (count != null && count > 0) {
            return;
        }

        jdbcTemplate.update(
                """
                INSERT INTO usuario (nome, email, senha_hash, status, papel_plataforma)
                VALUES (?, ?, ?, 'ATIVO', 'ADMIN_SAAS')
                """,
                name,
                email,
                passwordEncoder.encode(password));
        logger.info("Administrador SaaS inicial criado para {}.", email);
    }
}
