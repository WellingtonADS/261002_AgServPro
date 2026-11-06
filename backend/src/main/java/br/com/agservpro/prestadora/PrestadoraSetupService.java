package br.com.agservpro.prestadora;

import br.com.agservpro.security.AgServProUserDetails;
import br.com.agservpro.security.TenantRlsContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.ZoneId;
import java.util.UUID;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@Service
public class PrestadoraSetupService {
    private final JdbcTemplate jdbcTemplate;
    private final TenantRlsContext rlsContext;

    public PrestadoraSetupService(JdbcTemplate jdbcTemplate, TenantRlsContext rlsContext) {
        this.jdbcTemplate = jdbcTemplate;
        this.rlsContext = rlsContext;
    }

    @Transactional
    public SetupResponse complete(UUID prestadoraId, String providerName, String timezone, AgServProUserDetails director) {
        try {
            ZoneId.of(timezone);
        } catch (RuntimeException exception) {
            throw new ResponseStatusException(BAD_REQUEST, "Informe um fuso horário válido, como America/Manaus.");
        }
        rlsContext.useUsuario(director.id());
        Integer directorMembership = jdbcTemplate.queryForObject(
                "SELECT count(*) FROM prestadora_usuario WHERE prestadora_id = ? AND usuario_id = ? AND papel = 'DIRETOR' AND status = 'ATIVO'",
                Integer.class,
                prestadoraId,
                director.id());
        if (directorMembership == null || directorMembership != 1) {
            throw new ResponseStatusException(FORBIDDEN, "Apenas o diretor ativo pode concluir esta configuração.");
        }

        rlsContext.usePrestadora(prestadoraId, director.id());
        int updated = jdbcTemplate.update(
                "UPDATE prestadora SET nome = ?, fuso_horario = ?, status = 'ATIVA', atualizado_em = now() WHERE id = ? AND status = 'PENDENTE_CONFIGURACAO'",
                providerName.trim(),
                timezone,
                prestadoraId);
        if (updated != 1) {
            throw new ResponseStatusException(FORBIDDEN, "A prestadora já foi configurada ou não está disponível.");
        }
        return new SetupResponse(prestadoraId, providerName.trim(), timezone, "ATIVA");
    }

    public record SetupResponse(UUID prestadoraId, String nome, String fusoHorario, String status) {}
}
