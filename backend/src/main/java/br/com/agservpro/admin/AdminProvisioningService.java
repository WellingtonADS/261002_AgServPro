package br.com.agservpro.admin;

import br.com.agservpro.security.AgServProUserDetails;
import br.com.agservpro.security.TenantRlsContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.OffsetDateTime;
import java.util.Base64;
import java.util.HexFormat;
import java.util.Locale;
import java.util.UUID;

import static org.springframework.http.HttpStatus.CONFLICT;

@Service
public class AdminProvisioningService {
    private static final SecureRandom RANDOM = new SecureRandom();

    private final JdbcTemplate jdbcTemplate;
    private final TenantRlsContext rlsContext;
    private final String publicUrl;
    private final long invitationExpirationHours;

    public AdminProvisioningService(
            JdbcTemplate jdbcTemplate,
            TenantRlsContext rlsContext,
            @Value("${app.public-url}") String publicUrl,
            @Value("${app.invitation-expiration-hours:24}") long invitationExpirationHours) {
        this.jdbcTemplate = jdbcTemplate;
        this.rlsContext = rlsContext;
        this.publicUrl = publicUrl.replaceAll("/$", "");
        if (invitationExpirationHours < 1) {
            throw new IllegalArgumentException("A validade do convite deve ser de pelo menos uma hora.");
        }
        this.invitationExpirationHours = invitationExpirationHours;
    }

    @Transactional
    public ProvisionedDirector provisionDirector(String providerName, String directorName, String directorEmail, AgServProUserDetails actor) {
        rlsContext.useAdministradorSaas();
        String email = directorEmail.trim().toLowerCase(Locale.ROOT);
        Integer existing = jdbcTemplate.queryForObject(
                "SELECT count(*) FROM usuario WHERE lower(email) = ?", Integer.class, email);
        if (existing != null && existing > 0) {
            throw new ResponseStatusException(CONFLICT, "Já existe uma conta com este e-mail.");
        }

        UUID prestadoraId = jdbcTemplate.queryForObject(
                "INSERT INTO prestadora (nome) VALUES (?) RETURNING id", UUID.class, providerName.trim());
        UUID diretorId = jdbcTemplate.queryForObject(
                "INSERT INTO usuario (nome, email, status) VALUES (?, ?, 'PENDENTE') RETURNING id",
                UUID.class,
                directorName.trim(),
                email);
        jdbcTemplate.update(
                "INSERT INTO prestadora_usuario (prestadora_id, usuario_id, papel, status) VALUES (?, ?, 'DIRETOR', 'PENDENTE')",
                prestadoraId,
                diretorId);

        String token = createToken();
        String tokenHash = sha256(token);
        OffsetDateTime expiration = OffsetDateTime.now().plusHours(invitationExpirationHours);
        jdbcTemplate.update(
                """
                INSERT INTO convite_acesso (prestadora_id, usuario_id, papel, token_hash, expira_em, criado_por)
                VALUES (?, ?, 'DIRETOR', ?, ?, ?)
                """,
                prestadoraId,
                diretorId,
                tokenHash,
                expiration,
                actor.id());
        jdbcTemplate.update(
                """
                INSERT INTO auditoria_saas (ator_usuario_id, prestadora_id, acao, detalhes)
                VALUES (?, ?, 'PROVISIONAR_DIRETOR', jsonb_build_object('usuario_diretor_id', ?::uuid))
                """,
                actor.id(),
                prestadoraId,
                diretorId);

        return new ProvisionedDirector(
                prestadoraId,
                diretorId,
                publicUrl + "/convite?token=" + token,
                expiration);
    }

    private static String createToken() {
        byte[] bytes = new byte[32];
        RANDOM.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private static String sha256(String token) {
        try {
            byte[] hash = MessageDigest.getInstance("SHA-256").digest(token.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("SHA-256 não está disponível.", exception);
        }
    }

    public record ProvisionedDirector(UUID prestadoraId, UUID diretorId, String conviteUrl, OffsetDateTime expiraEm) {}
}
