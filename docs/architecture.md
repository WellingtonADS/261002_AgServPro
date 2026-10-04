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

## Modelo conceitual inicial

Entidades candidatas, sujeitas à definição do produto e da stack: Prestadora, Usuário, Cliente, Local de atendimento, Equipamento, Solicitação/OS, Orçamento, Item de orçamento, Agendamento, Registro de execução, Anexo e Histórico de alterações.

Antes de criar tabelas ou migrations, revisar o modelo com as regras de acesso e a decisão sobre SaaS multiempresa.
