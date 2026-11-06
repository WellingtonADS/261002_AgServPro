CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE usuario (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    nome varchar(160),
    email varchar(254) NOT NULL,
    senha_hash varchar(100),
    status varchar(20) NOT NULL DEFAULT 'PENDENTE',
    papel_plataforma varchar(30),
    criado_em timestamptz NOT NULL DEFAULT now(),
    atualizado_em timestamptz NOT NULL DEFAULT now(),
    CONSTRAINT ck_usuario_status CHECK (status IN ('PENDENTE', 'ATIVO', 'SUSPENSO')),
    CONSTRAINT ck_usuario_papel_plataforma CHECK (papel_plataforma IS NULL OR papel_plataforma = 'ADMIN_SAAS')
);

CREATE UNIQUE INDEX uq_usuario_email_normalizado ON usuario (lower(email));

CREATE TABLE prestadora (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    nome varchar(180) NOT NULL,
    status varchar(30) NOT NULL DEFAULT 'PENDENTE_CONFIGURACAO',
    fuso_horario varchar(64),
    criado_em timestamptz NOT NULL DEFAULT now(),
    atualizado_em timestamptz NOT NULL DEFAULT now(),
    CONSTRAINT ck_prestadora_status CHECK (status IN ('PENDENTE_CONFIGURACAO', 'ATIVA', 'SUSPENSA'))
);

CREATE TABLE prestadora_usuario (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    prestadora_id uuid NOT NULL REFERENCES prestadora(id),
    usuario_id uuid NOT NULL REFERENCES usuario(id),
    papel varchar(30) NOT NULL,
    status varchar(20) NOT NULL DEFAULT 'PENDENTE',
    criado_em timestamptz NOT NULL DEFAULT now(),
    atualizado_em timestamptz NOT NULL DEFAULT now(),
    CONSTRAINT uq_prestadora_usuario UNIQUE (prestadora_id, usuario_id),
    CONSTRAINT ck_prestadora_usuario_papel CHECK (papel IN ('DIRETOR', 'GERENCIA', 'COLABORADOR')),
    CONSTRAINT ck_prestadora_usuario_status CHECK (status IN ('PENDENTE', 'ATIVO', 'SUSPENSO'))
);

CREATE INDEX ix_prestadora_usuario_usuario_status
    ON prestadora_usuario (usuario_id, status);

CREATE TABLE convite_acesso (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    prestadora_id uuid NOT NULL REFERENCES prestadora(id),
    usuario_id uuid NOT NULL REFERENCES usuario(id),
    papel varchar(30) NOT NULL,
    token_hash char(64) NOT NULL UNIQUE,
    expira_em timestamptz NOT NULL,
    aceito_em timestamptz,
    revogado_em timestamptz,
    criado_por uuid NOT NULL REFERENCES usuario(id),
    criado_em timestamptz NOT NULL DEFAULT now(),
    CONSTRAINT ck_convite_papel CHECK (papel IN ('DIRETOR', 'GERENCIA', 'COLABORADOR'))
);

CREATE INDEX ix_convite_acesso_prestadora
    ON convite_acesso (prestadora_id, criado_em DESC);

CREATE TABLE auditoria_saas (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    ator_usuario_id uuid NOT NULL REFERENCES usuario(id),
    prestadora_id uuid REFERENCES prestadora(id),
    acao varchar(80) NOT NULL,
    detalhes jsonb NOT NULL DEFAULT '{}'::jsonb,
    criado_em timestamptz NOT NULL DEFAULT now()
);

CREATE INDEX ix_auditoria_saas_prestadora_data
    ON auditoria_saas (prestadora_id, criado_em DESC);

CREATE FUNCTION agservpro_context_admin()
RETURNS boolean
LANGUAGE sql
STABLE
AS $$
    SELECT COALESCE(current_setting('app.admin_saas', true), 'false') = 'true'
$$;

CREATE FUNCTION agservpro_context_prestadora()
RETURNS uuid
LANGUAGE sql
STABLE
AS $$
    SELECT NULLIF(current_setting('app.prestadora_id', true), '')::uuid
$$;

CREATE FUNCTION agservpro_context_usuario()
RETURNS uuid
LANGUAGE sql
STABLE
AS $$
    SELECT NULLIF(current_setting('app.usuario_id', true), '')::uuid
$$;

CREATE FUNCTION agservpro_context_convite()
RETURNS text
LANGUAGE sql
STABLE
AS $$
    SELECT NULLIF(current_setting('app.convite_token_hash', true), '')
$$;

ALTER TABLE prestadora ENABLE ROW LEVEL SECURITY;
ALTER TABLE prestadora FORCE ROW LEVEL SECURITY;
CREATE POLICY prestadora_scope ON prestadora
    USING (
        agservpro_context_admin()
        OR id = agservpro_context_prestadora()
        OR EXISTS (
            SELECT 1
            FROM prestadora_usuario pu
            WHERE pu.prestadora_id = prestadora.id
              AND pu.usuario_id = agservpro_context_usuario()
              AND pu.status = 'ATIVO'
        )
    )
    WITH CHECK (
        agservpro_context_admin()
        OR (
            id = agservpro_context_prestadora()
            AND EXISTS (
                SELECT 1
                FROM prestadora_usuario pu
                WHERE pu.prestadora_id = prestadora.id
                  AND pu.usuario_id = agservpro_context_usuario()
                  AND pu.status = 'ATIVO'
            )
        )
    );

ALTER TABLE prestadora_usuario ENABLE ROW LEVEL SECURITY;
ALTER TABLE prestadora_usuario FORCE ROW LEVEL SECURITY;
CREATE POLICY prestadora_usuario_scope ON prestadora_usuario
    USING (agservpro_context_admin() OR usuario_id = agservpro_context_usuario())
    WITH CHECK (agservpro_context_admin());

ALTER TABLE convite_acesso ENABLE ROW LEVEL SECURITY;
ALTER TABLE convite_acesso FORCE ROW LEVEL SECURITY;
CREATE POLICY convite_acesso_scope ON convite_acesso
    USING (
        agservpro_context_admin()
        OR token_hash = agservpro_context_convite()
        OR (
            prestadora_id = agservpro_context_prestadora()
            AND EXISTS (
                SELECT 1
                FROM prestadora_usuario pu
                WHERE pu.prestadora_id = convite_acesso.prestadora_id
                  AND pu.usuario_id = agservpro_context_usuario()
                  AND pu.status = 'ATIVO'
            )
        )
    )
    WITH CHECK (
        agservpro_context_admin()
        OR (
            prestadora_id = agservpro_context_prestadora()
            AND EXISTS (
                SELECT 1
                FROM prestadora_usuario pu
                WHERE pu.prestadora_id = convite_acesso.prestadora_id
                  AND pu.usuario_id = agservpro_context_usuario()
                  AND pu.status = 'ATIVO'
            )
        )
    );

ALTER TABLE auditoria_saas ENABLE ROW LEVEL SECURITY;
ALTER TABLE auditoria_saas FORCE ROW LEVEL SECURITY;
CREATE POLICY auditoria_saas_scope ON auditoria_saas
    USING (agservpro_context_admin())
    WITH CHECK (agservpro_context_admin());
