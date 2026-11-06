package br.com.agservpro.auth;

import br.com.agservpro.security.TenantRlsContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.OffsetDateTime;
import java.util.HexFormat;
import java.util.UUID;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.GONE;

@Service
public class AuthenticationService {
    private final JdbcTemplate jdbcTemplate;
    private final TenantRlsContext rlsContext;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationService(JdbcTemplate jdbcTemplate, TenantRlsContext rlsContext, PasswordEncoder passwordEncoder) {
        this.jdbcTemplate = jdbcTemplate;
        this.rlsContext = rlsContext;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void acceptInvitation(String token, String name, String password) {
        if (password.getBytes(StandardCharsets.UTF_8).length > 72) {
            throw new ResponseStatusException(BAD_REQUEST, "A senha excede o limite seguro de tamanho.");
        }
        String tokenHash = sha256(token);
        rlsContext.useConvite(tokenHash);

        Invitation invitation = jdbcTemplate.query(
                        """
                        SELECT prestadora_id, usuario_id, expira_em, aceito_em, revogado_em
                        FROM convite_acesso
                        WHERE token_hash = ?
                        """,
                        (resultSet, rowNumber) -> new Invitation(
                                resultSet.getObject("prestadora_id", UUID.class),
                                resultSet.getObject("usuario_id", UUID.class),
                                resultSet.getObject("expira_em", OffsetDateTime.class),
                                resultSet.getObject("aceito_em", OffsetDateTime.class),
                                resultSet.getObject("revogado_em", OffsetDateTime.class)),
                        tokenHash)
                .stream()
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, "Convite inválido."));

        if (invitation.acceptedAt() != null || invitation.revokedAt() != null) {
            throw new ResponseStatusException(GONE, "Este convite já foi utilizado ou revogado.");
        }
        if (invitation.expiresAt().isBefore(OffsetDateTime.now())) {
            throw new ResponseStatusException(GONE, "Este convite expirou.");
        }

        rlsContext.useAdministradorSaas();
        int updatedUser = jdbcTemplate.update(
                """
                UPDATE usuario
                SET nome = ?, senha_hash = ?, status = 'ATIVO', atualizado_em = now()
                WHERE id = ? AND status = 'PENDENTE' AND senha_hash IS NULL
                """,
                name.trim(),
                passwordEncoder.encode(password),
                invitation.usuarioId());
        if (updatedUser != 1) {
            throw new ResponseStatusException(GONE, "A conta deste convite já foi ativada.");
        }

        jdbcTemplate.update(
                "UPDATE prestadora_usuario SET status = 'ATIVO', atualizado_em = now() WHERE prestadora_id = ? AND usuario_id = ? AND status = 'PENDENTE'",
                invitation.prestadoraId(),
                invitation.usuarioId());
        jdbcTemplate.update(
                "UPDATE convite_acesso SET aceito_em = now() WHERE token_hash = ? AND aceito_em IS NULL AND revogado_em IS NULL",
                tokenHash);
    }

    private static String sha256(String value) {
        try {
            return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(value.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("SHA-256 não está disponível.", exception);
        }
    }

    private record Invitation(UUID prestadoraId, UUID usuarioId, OffsetDateTime expiresAt, OffsetDateTime acceptedAt, OffsetDateTime revokedAt) {}
}
