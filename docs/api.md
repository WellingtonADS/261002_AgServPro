# API do AgServPro

## Estado atual

A API usa REST/JSON e o prefixo versionado `/api/v1`. O único endpoint implementado hoje é `GET /api/v1/health`; os demais caminhos abaixo são propostas para o MVP, ainda não implementadas.

O contrato OpenAPI dos endpoints existentes fica em [`backend/openapi.yaml`](../backend/openapi.yaml). As rotas planejadas só devem entrar no OpenAPI quando seu contrato for aprovado.

## Executar localmente

Com Java 21 e Maven instalados, inicie a API a partir da raiz do repositório:

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

A forma de autenticação ainda não foi escolhida. Portanto, os códigos de autenticação estão previstos, mas os endpoints de login e sessão ficam para depois dessa decisão.

## Primeira fatia: clientes e ordens de serviço

| Método e rota proposta | Uso |
|---|---|
| `GET /api/v1/customers?search=&page=&size=` | Pesquisar clientes por nome ou telefone |
| `POST /api/v1/customers` | Cadastrar cliente e contato principal |
| `GET /api/v1/customers/{customerId}` | Consultar cliente, contatos, locais e equipamentos |
| `PATCH /api/v1/customers/{customerId}` | Atualizar dados permitidos do cliente |
| `GET /api/v1/service-orders?status=&customerId=&equipmentId=&page=&size=` | Consultar a fila e o histórico |
| `POST /api/v1/service-orders` | Abrir solicitação/OS para cliente existente |
| `GET /api/v1/service-orders/{orderId}` | Consultar os dados e o estado atual da OS |
| `PATCH /api/v1/service-orders/{orderId}` | Corrigir relato, serviço solicitado, local ou equipamento |
| `POST /api/v1/service-orders/{orderId}/transitions` | Solicitar uma mudança de estado validada pelo backend |

Para manter o primeiro corte simples, a proposta trata a solicitação inicial como uma OS no estado `SOLICITADO` ou `AGUARDANDO_INFORMACOES`; não cria uma segunda entidade `request`. A regra exata para escolher o estado e o conjunto de transições precisa acompanhar o fechamento de [Regras de negócio](business-rules.md).

A abertura usa cliente já cadastrado. Para o fluxo rápido do WhatsApp, a interface pode cadastrar o cliente e depois criar a OS usando os dois endpoints. O cliente pode ser criado sem endereço ou equipamento.

Exemplo proposto de criação:

```http
POST /api/v1/service-orders
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

O backend define `status` e `providerId`. Eles não são aceitos do corpo da requisição. Campos obrigatórios e regra de estado inicial serão fechados com os critérios TA-03, TA-04 e TA-15.

## Próximas rotas do MVP

| Recurso | Rotas propostas | Requisitos relacionados |
|---|---|---|
| Orçamentos | `GET/POST /api/v1/service-orders/{orderId}/budgets`; `GET/PATCH /api/v1/budgets/{budgetId}`; `POST /api/v1/budgets/{budgetId}/decisions` | TA-06 e TA-10 |
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
2. Confirmar que a solicitação inicial e a OS são o mesmo recurso.
3. Fechar autenticação, vínculo usuário–prestadora e regra de isolamento.
4. Definir campos obrigatórios e estado inicial de uma OS incompleta.
5. Definir o formato comum de paginação e erro.

## Manutenção

Ao implementar um endpoint, atualize `backend/openapi.yaml` junto com o código e mantenha esta página sincronizada. Não marque rotas propostas como disponíveis antes de existirem na aplicação.
