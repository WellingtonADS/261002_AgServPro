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

## Recomendação técnica para o MVP — aguardando aprovação

| Camada | Recomendação | Motivo |
|---|---|---|
| Backend | Java 21 + Spring Boot, como monólito modular | Aproveita a experiência existente do projeto com Java 21 e mantém regras, autenticação e integrações no backend. |
| API | REST/JSON, versionada em `/api/v1`, descrita com OpenAPI | Atende formulários, agenda e fluxo operacional; mantém contrato claro entre frontend e backend. |
| Banco | PostgreSQL + Flyway | Relacional, adequado aos vínculos entre prestadoras, usuários, clientes, solicitações e OS; migrations versionadas. |
| Frontend | Vue 3 + TypeScript + Vite + Vue Router | Interface responsiva separada da API; Vite evita adicionar renderização de servidor, que não é necessária para o painel operacional. |
| Componentes visuais | PrimeVue com tema personalizado | Acelera formulários, calendário, tabelas e diálogos, permitindo aproximar cores e estados do protótipo aprovado. |
| Design | Usar o protótipo HTML aprovado como referência visual | Preserva o fluxo validado; definir tokens e componentes compartilhados durante a implementação. Figma fica opcional. |

Para o isolamento multiempresa, cada dado operacional terá `prestadora_id`. O backend obterá a prestadora a partir da identidade autenticada e aplicará esse escopo em todas as operações. O identificador enviado pelo navegador não será suficiente para conceder acesso.

**Ordem da primeira entrega:** autenticação e vínculo usuário–prestadora; cadastro de cliente; abertura de solicitação rápida. A parte funcional segue TA-01, TA-03, TA-04 e TA-15. O layout final será aplicado após a aprovação do protótipo.

Hospedagem, domínio e estratégia final de sessão ainda precisam ser definidos antes do deploy. Esta stack é uma recomendação e depende de aprovação antes do scaffold.

Referências: [Spring Boot](https://spring.io/projects/spring-boot), [Vue com TypeScript](https://vuejs.org/guide/typescript/overview), [Vite](https://vite.dev/guide/), [PrimeVue](https://primevue.dev/components/), [OpenAPI](https://www.openapis.org/).

## Próximo incremento enquanto o protótipo é validado

O primeiro fluxo técnico pode ser preparado sem depender da aprovação visual: cadastro do cliente e abertura rápida de uma solicitação. O fluxo deve aceitar equipamento e endereço ainda desconhecidos, manter a solicitação pendente de informações e impedir que ela siga para execução até estar pronta.

Critérios já definidos para esse incremento: TA-01, TA-03, TA-04 e TA-15 em `requirements.md`; RN-01 e RN-02 em `business-rules.md`. Assim, não é necessário criar outra especificação para esse fluxo.

Antes de criar tabelas, migrations ou autenticação, aprovar a stack e a estratégia de sessão. A camada visual do fluxo será ajustada ao protótipo depois da aprovação.

## Modelo conceitual inicial

Entidades candidatas, sujeitas à definição do produto e da stack: Prestadora, Usuário, Cliente, Local de atendimento, Equipamento, Solicitação/OS, Orçamento, Item de orçamento, Agendamento, Registro de execução, Anexo e Histórico de alterações.

Antes de criar tabelas ou migrations, revisar o modelo com as regras de acesso e a decisão sobre SaaS multiempresa.
