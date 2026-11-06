# API do AgServPro

## Estado atual

A API usa REST/JSON e o prefixo versionado `/api/v1`. Estão implementados saúde, sessão por e-mail e senha, aceite de convite, provisionamento do diretor pelo Administrador SaaS e configuração inicial da prestadora.

O contrato OpenAPI dos endpoints existentes fica em [`backend/openapi.yaml`](../backend/openapi.yaml). As rotas planejadas só devem entrar no OpenAPI quando seu contrato for aprovado.

## Executar localmente

Com Docker, Java 21 e Maven instalados, inicie o PostgreSQL pela raiz do repositório:

```bash
docker compose up -d
```

Configure `ADMIN_SAAS_EMAIL` e `ADMIN_SAAS_PASSWORD` para criar o primeiro Administrador SaaS. Depois, inicie a API:

```bash
mvn -f backend/pom.xml spring-boot:run
```

A porta padrão é `8080`; a configuração permite sobrescrevê-la pela variável de ambiente `PORT`.

## Endpoint implementado

### Verificar disponibilidade da aplicação

`GET /api/v1/health`

Não exige parâmetros nem corpo de requisição.

```bash
curl -i http://localhost:8080/api/v1/health
```

Resposta atual:

```http
HTTP/1.1 200
Content-Type: application/json
```

```json
{
  "status": "UP"
}
```

O valor `UP` é retornado pelo controller. Este endpoint confirma que a aplicação responde; não verifica conexão com banco ou outros serviços.

## Convenções propostas para o MVP

- Nomes de recursos em inglês na URL e propriedades JSON em `camelCase`, seguindo o padrão técnico já iniciado em `/health` e `status`.
- IDs UUID em caminhos de recursos.
- Datas e horários em ISO 8601 com fuso explícito.
- Coleções paginadas com `page` (começando em 0) e `size` (padrão 20, máximo proposto 100).
- `POST` cria recursos e retorna `201 Created` com `Location`; `GET` retorna `200 OK`; `PATCH` altera campos permitidos.
- Estados de OS são alterados por operação própria, com validação no backend; o cliente não define o estado inicial.
- Endpoints de negócio serão autenticados. O backend obterá a prestadora do usuário autenticado, sem aceitar `providerId` do corpo ou da URL.
- Quando um recurso existir em outra prestadora, responder como não encontrado (`404`) para não revelar sua existência.

## Acesso e primeiro provisionamento

O login usa sessão no cookie `AGSERVPRO_SESSION` (HttpOnly). Chamadas de escrita exigem o token CSRF obtido em `GET /api/v1/auth/csrf`, enviado no header `X-XSRF-TOKEN`. O Administrador SaaS inicial é criado no primeiro início quando `ADMIN_SAAS_EMAIL` e `ADMIN_SAAS_PASSWORD` estão configurados; a senha deve ter pelo menos 12 caracteres.

| Método e rota | Uso |
|---|---|
| `GET /api/v1/auth/csrf` | Obter token CSRF para chamadas de escrita |
| `POST /api/v1/auth/session` | Entrar com e-mail e senha |
| `DELETE /api/v1/auth/session` | Encerrar sessão |
| `GET /api/v1/auth/me` | Consultar identidade e vínculos com prestadoras |
| `POST /api/v1/admin/prestadoras` | Administrador SaaS cria prestadora pendente e provisiona diretor |
| `POST /api/v1/auth/invitations/accept` | Diretor define senha e aceita convite de uso único; retorna `204` |
| `PUT /api/v1/prestadoras/{prestadoraId}/configuracao` | Diretor conclui nome e fuso horário da prestadora |

A URL do convite é devolvida somente na resposta de provisionamento e expira em 24 horas. O banco armazena somente o hash do token. O diretor aceita o convite na tela `/convite`; não há envio automático de e-mail.

## Primeira fatia: clientes e solicitações

| Método e rota proposta | Uso |
|---|---|
| `GET /api/v1/customers?search=&page=&size=` | Pesquisar clientes por nome ou telefone |
| `POST /api/v1/customers` | Cadastrar cliente e contato principal |
| `GET /api/v1/customers/{customerId}` | Consultar cliente, contatos, locais e equipamentos |
| `PATCH /api/v1/customers/{customerId}` | Atualizar dados permitidos do cliente |
| `GET /api/v1/service-requests?status=&customerId=&page=&size=` | Consultar solicitações e fila de triagem |
| `POST /api/v1/service-requests` | Registrar solicitação para cliente existente |
| `GET /api/v1/service-requests/{requestId}` | Consultar solicitação, triagem, orçamento e eventual OS vinculada |
| `PATCH /api/v1/service-requests/{requestId}` | Atualizar relato, serviço pedido, local ou equipamento enquanto permitido |
| `POST /api/v1/service-requests/{requestId}/triage` | Registrar resultado da triagem e indicar se orçamento é necessário |
| `GET/POST /api/v1/service-requests/{requestId}/budgets` | Consultar ou criar orçamento da solicitação |
| `POST /api/v1/service-requests/{requestId}/conversion` | Converter em OS quando a triagem e as aprovações necessárias permitirem |


A solicitação e a OS são recursos separados. A solicitação é criada primeiro e passa pela triagem; se houver orçamento necessário, a conversão em OS só ocorre após sua aprovação. Quando a triagem indicar que não é necessário orçamento, a solicitação pode ser convertida após a triagem. Não há endpoint de criação direta de OS na primeira etapa. A recusa do orçamento arquiva a solicitação sem criar OS.

A solicitação usa cliente já cadastrado. No fluxo rápido do WhatsApp, a interface pode cadastrar o cliente e depois registrar a solicitação. O cliente e a solicitação podem não ter endereço ou equipamento no início; isso não cria nem libera uma OS.

Exemplo proposto de criação de solicitação:

```http
POST /api/v1/service-requests
Content-Type: application/json
```

```json
{
  "customerId": "9bb6c488-0bd2-4f30-ae3a-68839286e258",
  "source": "WHATSAPP",
  "requestedService": "Limpeza de ar-condicionado",
  "customerReport": "O aparelho está pingando água.",
  "locationId": null,
  "equipmentId": null
}
```

O backend define o estado inicial e o escopo da prestadora; não aceita `status` ou `providerId` do corpo. A criação retorna uma solicitação, nunca uma OS. A operação de conversão deve verificar no backend a triagem concluída e a aprovação do orçamento quando exigido. Para reutilizar uma solicitação arquivada, a interface abre um novo formulário preenchido com seu conteúdo editável, incluindo triagem e itens do orçamento recusado; o usuário pode ajustar os dados antes de salvar. A criação usa `POST /service-requests`, recebe novo ID e estado `RECEBIDA`, e pode incluir `sourceRequestId` para registrar a origem. Orçamento copiado é criado como novo rascunho, sem decisão anterior. OS, agendamentos, execuções, links e eventos não são copiados; a origem permanece intacta.

### Cadastro completo do cliente

Para manter o primeiro cadastro enxuto, cliente, contato principal e solicitação podem ser criados no fluxo inicial. Contatos, locais e equipamentos adicionais usam sub-recursos:

| Método e rota proposta | Uso |
|---|---|
| `POST /api/v1/customers/{customerId}/contacts` | Adicionar contato |
| `PATCH /api/v1/customers/{customerId}/contacts/{contactId}` | Atualizar contato |
| `POST /api/v1/customers/{customerId}/locations` | Adicionar local de atendimento |
| `PATCH /api/v1/customers/{customerId}/locations/{locationId}` | Atualizar local |
| `POST /api/v1/customers/{customerId}/equipment` | Cadastrar equipamento |
| `PATCH /api/v1/customers/{customerId}/equipment/{equipmentId}` | Atualizar equipamento |

Os detalhes desses sub-recursos também aparecem na consulta de `GET /customers/{customerId}`. Exclusão física não está prevista; a regra para inativação ainda precisa ser definida.

## Próximas rotas do MVP

| Recurso | Rotas propostas | Requisitos relacionados |
|---|---|---|
| Orçamentos | `GET/POST /api/v1/service-requests/{requestId}/budgets`; após conversão, decisões de serviço adicional podem ser vinculadas à OS | TA-06 e TA-10 |
| Ordens de serviço | `GET /api/v1/service-orders?status=&customerId=&page=&size=`; `GET /api/v1/service-orders/{orderId}`; `POST /api/v1/service-requests/{requestId}/conversion` | TA-19 |
| Agenda | `GET /api/v1/appointments?from=&to=&technicianId=&status=`; `POST /api/v1/service-orders/{orderId}/appointments`; `GET/PATCH /api/v1/appointments/{appointmentId}` | TA-07, RN-10 a RN-13 |
| Execução | `GET/POST /api/v1/service-orders/{orderId}/executions`; `PATCH /api/v1/executions/{executionId}` | TA-08, TA-09, TA-11 e TA-12 |
| Histórico | `GET /api/v1/service-orders/{orderId}/events` | TA-13 e TA-14 |

Conflitos de agenda devem retornar `409 Conflict`; a validação precisa ocorrer no backend e no banco para cobrir agendamentos concorrentes. As rotas de anexos dependem da definição de armazenamento. Os links públicos do cliente dependem da política de token, expiração e revogação.

## Respostas de erro propostas

- `400 Bad Request`: corpo inválido ou campo obrigatório ausente.
- `401 Unauthorized`: usuário não autenticado.
- `403 Forbidden`: usuário autenticado sem permissão para a ação.
- `404 Not Found`: recurso inexistente ou fora da prestadora do usuário.
- `409 Conflict`: transição inválida ou conflito de horário.

O formato do corpo de erro deve ser padronizado antes da implementação dos endpoints de negócio.

## Decisões para fechar antes do contrato OpenAPI da primeira fatia

1. Confirmar nomes em inglês para rotas e JSON.
2. A separação solicitação/OS, o arquivamento após recusa e o comportamento da cópia total estão definidos; fechar payloads e estados da conversão.
3. Validar a matriz completa de permissões e implementar gestão de acessos de gerência e colaborador.
4. Definir campos obrigatórios e estado inicial da solicitação incompleta.
6. Definir o formato comum de paginação e erro.

## Manutenção

Ao implementar um endpoint, atualize `backend/openapi.yaml` junto com o código e mantenha esta página sincronizada. Não marque rotas propostas como disponíveis antes de existirem na aplicação.
