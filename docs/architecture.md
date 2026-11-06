# Arquitetura e decisões técnicas

**Status geral:** SaaS multiempresa e stack aprovados; autenticação, deploy e mecanismos finais de isolamento ainda pendentes.

## Proposta atual

- Aplicação web responsiva para computador e celular.
- SaaS para várias prestadoras, com isolamento dos dados por prestadora desde a primeira versão.
- Monólito modular para manter simples o desenvolvimento e a operação inicial.
- Banco relacional PostgreSQL com migrations versionadas por Flyway.
- Perfis na plataforma: administrador SaaS. Perfis dentro de cada prestadora: diretor, gerência e colaborador. As permissões detalhadas ainda precisam ser definidas.
- Cliente final sem conta; consulta e resposta ao orçamento por link.
- Operação online no MVP; modo offline não foi aprovado.
- Fotos e anexos vinculados à OS, em armazenamento separado do banco.

## Decisões pendentes

1. Forma de autenticação e entrega segura do acesso inicial; decidir se o registro preliminar da prestadora é criado pelo admin SaaS ou pelo diretor durante a configuração; validar a matriz de permissões por perfil.
2. Como aplicar e validar o isolamento por prestadora no banco e na aplicação.
3. Expiração, revogação e proteção do link do cliente.
4. Armazenamento, limites e retenção de fotos/anexos.
5. Quantidade de técnicos cadastráveis no MVP; conflito se aplica ao técnico atribuído.
6. Hospedagem e deploy.
7. Confirmar se conexão contínua atende ao trabalho em campo.

## Decisão aprovada

- O produto será SaaS multiempresa desde o MVP. Os registros operacionais devem pertencer a uma prestadora, e o acesso de uma prestadora não pode expor dados de outra.
- Solicitação e OS são registros distintos. A OS só é criada após a triagem e, quando o orçamento for necessário, após sua aprovação. Decisão confirmada pelo responsável em 2026-11-06.
- Quando o cliente recusa o orçamento, a solicitação é arquivada sem gerar OS. Ela pode ser usada como modelo para criar uma solicitação nova, sem alterar a original.
- O administrador do sistema SaaS provisiona o acesso inicial do diretor; o diretor conclui a configuração da prestadora e cria os acessos de gerência e colaborador. Decisão confirmada pelo responsável em 2026-11-06.
- A decisão foi confirmada pelo responsável em 2026-10-08. A tecnologia usada para impor esse isolamento permanece pendente.
- Stack aprovada pelo responsável em 2026-10-08: Java 21 + Spring Boot; API REST/JSON documentada com OpenAPI; PostgreSQL + Flyway; Vue 3 + TypeScript + Vite + Vue Router; PrimeVue com tema personalizado. O protótipo HTML continua em validação e será referência visual após aprovação.

## Arquitetura aprovada para o MVP

| Camada | Decisão | Motivo |
|---|---|---|
| Backend | Java 21 + Spring Boot, como monólito modular | Aproveita a experiência existente do projeto com Java 21 e mantém regras, autenticação e integrações no backend. |
| API | REST/JSON, versionada em `/api/v1`, descrita com OpenAPI | Atende formulários, agenda e fluxo operacional; mantém contrato claro entre frontend e backend. |
| Banco | PostgreSQL + Flyway | Relacional, adequado aos vínculos entre prestadoras, usuários, clientes, solicitações e OS; migrations versionadas. |
| Frontend | Vue 3 + TypeScript + Vite + Vue Router | Interface responsiva separada da API; Vite evita adicionar renderização de servidor, que não é necessária para o painel operacional. |
| Componentes visuais | PrimeVue com tema personalizado | Acelera formulários, calendário, tabelas e diálogos, permitindo aproximar cores e estados do protótipo aprovado. |
| Design | Usar o protótipo HTML em validação como referência visual após aprovação | Preserva o fluxo validado; definir tokens e componentes compartilhados durante a implementação. Figma fica opcional. |

Para o isolamento multiempresa, cada dado operacional terá `prestadora_id`. O backend obterá a prestadora a partir da identidade autenticada e aplicará esse escopo em todas as operações. O identificador enviado pelo navegador não será suficiente para conceder acesso.

**Ordem da primeira entrega:** autenticação e vínculo usuário–prestadora; cadastro de cliente; abertura de solicitação rápida. A parte funcional segue TA-01, TA-03, TA-04 e TA-15. O layout final será aplicado após a aprovação do protótipo.

Hospedagem, domínio e estratégia final de sessão ainda precisam ser definidos antes do deploy. Esta stack foi aprovada em 2026-10-08.

Referências: [Spring Boot](https://spring.io/projects/spring-boot), [Vue com TypeScript](https://vuejs.org/guide/typescript/overview), [Vite](https://vite.dev/guide/), [PrimeVue](https://primevue.dev/components/), [OpenAPI](https://www.openapis.org/).

## Próximo incremento enquanto o protótipo é validado

O primeiro fluxo técnico pode ser preparado sem depender da aprovação visual: cadastro do cliente e abertura rápida de uma solicitação. O fluxo deve aceitar equipamento e endereço ainda desconhecidos, manter a solicitação pendente de informações e impedir que ela siga para execução até estar pronta.

Critérios já definidos para esse incremento: TA-01, TA-03, TA-04 e TA-15 em `requirements.md`; RN-01 e RN-02 em `business-rules.md`. Assim, não é necessário criar outra especificação para esse fluxo.

Antes de implementar autenticação e autorização, definir a forma segura de provisionar o acesso inicial do diretor, a conclusão da configuração da prestadora e a concessão de acessos de gerência e colaborador. A camada visual do fluxo será ajustada ao protótipo depois da aprovação.

## Modelo de dados

A proposta lógica de entidades, vínculos, integridade, índices e pendências está em [Modelo lógico de dados](architecture/data-model.md). Ela ainda precisa ser revisada antes de criar tabelas ou migrations, especialmente quanto ao modelo de isolamento multiempresa, autenticação e estados da OS.

## Camadas de negócio e organização do backend (proposta)

Esta separação descreve o destino do backend; ainda não está implementada. A organização seguirá o monólito modular aprovado, com módulos agrupados por funcionalidade e regras de negócio protegidas da tecnologia de transporte e persistência.

| Camada | Responsabilidade |
|---|---|
| API / apresentação | Receber chamadas REST, validar formato dos dados, chamar um caso de uso e montar a resposta HTTP. Não concentra regras do negócio. |
| Aplicação | Executar casos de uso e coordenar etapas da operação, transação e acesso aos dados. |
| Domínio | Representar conceitos e aplicar regras e transições do negócio, como quando uma solicitação pode avançar para execução. Deve evitar dependência de Spring, HTTP ou banco. |
| Infraestrutura | Implementar persistência e integrações usadas pelos casos de uso, inicialmente PostgreSQL/Flyway quando essa parte for construída. |

O fluxo esperado é: endpoint → caso de uso → regras do domínio → persistência → resposta. Exemplo: abrir solicitação recebe os dados pela API, executa as validações e regras definidas para a solicitação, salva o resultado e devolve a resposta. Os critérios funcionais permanecem em `requirements.md` e `business-rules.md`; este desenho não os duplica.

Os módulos devem acompanhar as funcionalidades necessárias, por exemplo `clientes` e `solicitacoes` no primeiro incremento. Outras áreas, como agenda, orçamento e execução de OS, entram quando forem implementadas. Cada módulo mantém seus componentes próximos; código compartilhado só deve ser extraído quando houver reutilização real.

O escopo da prestadora continua sendo obtido no backend a partir da identidade autenticada, conforme a decisão acima. O mecanismo concreto de autenticação e isolamento segue pendente. O backend atual contém apenas a aplicação Spring Boot e o endpoint de saúde; estas camadas são uma proposta, não uma descrição do código existente.
