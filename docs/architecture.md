# Arquitetura e decisões técnicas

**Status geral:** SaaS multiempresa aprovado; stack e mecanismos técnicos ainda pendentes.

## Proposta atual

- Aplicação web responsiva para computador e celular.
- SaaS para várias prestadoras, com isolamento dos dados por prestadora desde a primeira versão.
- Monólito modular para manter simples o desenvolvimento e a operação inicial.
- PostgreSQL como banco proposto.
- Perfis propostos: proprietário/gestor e técnico.
- Cliente final sem conta; consulta e resposta ao orçamento por link.
- Operação online no MVP; modo offline não foi aprovado.
- Fotos e anexos vinculados à OS, em armazenamento separado do banco.

## Decisões pendentes

1. Stack, hospedagem e deploy.
2. Como aplicar e validar o isolamento por prestadora no banco e na aplicação.
3. Autenticação e permissões para proprietário, técnico e cliente por link.
4. Expiração, revogação e proteção do link do cliente.
5. Armazenamento, limites e retenção de fotos/anexos.
6. Quantidade de técnicos cadastráveis no MVP; conflito se aplica ao técnico atribuído.
7. Confirmar se conexão contínua atende ao trabalho em campo.

## Decisão aprovada

- O produto será SaaS multiempresa desde o MVP. Os registros operacionais devem pertencer a uma prestadora, e o acesso de uma prestadora não pode expor dados de outra.
- A decisão foi confirmada pelo responsável em 2026-10-08. A tecnologia usada para impor esse isolamento permanece pendente.

## Próximo incremento enquanto o protótipo é validado

O primeiro fluxo técnico pode ser preparado sem depender da aprovação visual: cadastro do cliente e abertura rápida de uma solicitação. O fluxo deve aceitar equipamento e endereço ainda desconhecidos, manter a solicitação pendente de informações e impedir que ela siga para execução até estar pronta.

Critérios já definidos para esse incremento: TA-01, TA-03, TA-04 e TA-15 em `requirements.md`; RN-01 e RN-02 em `business-rules.md`. Assim, não é necessário criar outra especificação para esse fluxo.

Antes de criar tabelas, migrations ou autenticação, escolher a stack e definir como o contexto da prestadora será validado em todas as operações. A camada visual do fluxo será ajustada ao protótipo depois da aprovação.

## Modelo conceitual inicial

Entidades candidatas, sujeitas à definição do produto e da stack: Prestadora, Usuário, Cliente, Local de atendimento, Equipamento, Solicitação/OS, Orçamento, Item de orçamento, Agendamento, Registro de execução, Anexo e Histórico de alterações.

Antes de criar tabelas ou migrations, revisar o modelo com as regras de acesso e a decisão sobre SaaS multiempresa.
