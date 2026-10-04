# Arquitetura e decisões técnicas

**Status geral:** proposta inicial; não tratar como arquitetura aprovada até confirmar os itens pendentes.

## Proposta atual

- Aplicação web responsiva para computador e celular.
- Monólito modular para manter simples o desenvolvimento e a operação inicial.
- PostgreSQL como banco proposto.
- Perfis propostos: proprietário/gestor e técnico.
- Cliente final sem conta; consulta e resposta ao orçamento por link.
- Operação online no MVP; modo offline não foi aprovado.
- Fotos e anexos vinculados à OS, em armazenamento separado do banco.

## Decisões pendentes

1. SaaS para várias prestadoras foi uma premissa inicial e precisa de confirmação.
2. Stack, hospedagem e deploy ainda não foram definidos.
3. Autenticação e permissões para proprietário, técnico e cliente por link.
4. Expiração, revogação e proteção do link do cliente.
5. Armazenamento, limites e retenção de fotos/anexos.
6. Quantidade de técnicos cadastráveis no MVP; conflito se aplica ao técnico atribuído.
7. Confirmar se conexão contínua atende ao trabalho em campo.

## Próximo incremento enquanto o protótipo é validado

O primeiro fluxo técnico pode ser preparado sem depender da aprovação visual: cadastro do cliente e abertura rápida de uma solicitação. O fluxo deve aceitar equipamento e endereço ainda desconhecidos, manter a solicitação pendente de informações e impedir que ela siga para execução até estar pronta.

Critérios já definidos para esse incremento: TA-01, TA-03, TA-04 e TA-15 em `requirements.md`; RN-01 e RN-02 em `business-rules.md`. Assim, não é necessário criar outra especificação para esse fluxo.

Antes de criar tabelas, migrations ou autenticação, confirmar se o MVP será SaaS para várias prestadoras e escolher a stack. Se multiempresa for confirmada, cada registro operacional deverá ficar isolado por prestadora desde o início. A camada visual do fluxo será ajustada ao protótipo depois da aprovação.

## Modelo conceitual inicial

Entidades candidatas, sujeitas à definição do produto e da stack: Prestadora, Usuário, Cliente, Local de atendimento, Equipamento, Solicitação/OS, Orçamento, Item de orçamento, Agendamento, Registro de execução, Anexo e Histórico de alterações.

Antes de criar tabelas ou migrations, revisar o modelo com as regras de acesso e a decisão sobre SaaS multiempresa.
