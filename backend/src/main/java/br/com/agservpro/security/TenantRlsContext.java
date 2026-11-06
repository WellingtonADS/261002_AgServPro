package br.com.agservpro.security;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.UUID;

@Component
public class TenantRlsContext {
    private final JdbcTemplate jdbcTemplate;

    public TenantRlsContext(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void usePrestadora(UUID prestadoraId) {
        requireTransaction();
        jdbcTemplate.queryForObject(
                "select set_config('app.admin_saas', 'false', true)", String.class);
        jdbcTemplate.queryForObject(
                "select set_config('app.prestadora_id', ?, true)",
                String.class,
                prestadoraId.toString());
    }

    public void useAdministradorSaas() {
        requireTransaction();
        jdbcTemplate.queryForObject(
                "select set_config('app.prestadora_id', '', true)", String.class);
        jdbcTemplate.queryForObject(
                "select set_config('app.admin_saas', 'true', true)", String.class);
    }

    private void requireTransaction() {
        if (!TransactionSynchronizationManager.isActualTransactionActive()) {
            throw new IllegalStateException("O contexto RLS deve ser definido dentro de uma transação.");
        }
    }
}
