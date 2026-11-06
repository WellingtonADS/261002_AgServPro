# API do AgServPro

## Estado atual

A API está na versão inicial `/api/v1`. Neste momento, o único endpoint implementado é o health check. Contratos de clientes, solicitações, orçamento, agenda e ordem de serviço ainda não foram definidos neste arquivo porque esses endpoints não existem no código.

O contrato OpenAPI fica em [`backend/openapi.yaml`](../backend/openapi.yaml).

## Executar localmente

Com Java 21 e Maven instalados, inicie a API a partir da raiz do repositório:

```bash
mvn -f backend/pom.xml spring-boot:run
```

A porta padrão é `8080`; a configuração permite sobrescrevê-la pela variável de ambiente `PORT`.

## Endpoint disponível

### Verificar disponibilidade da aplicação

`GET /api/v1/health`

Não exige parâmetros nem corpo de requisição.

Exemplo:

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

O valor `UP` é retornado pelo controller. Este endpoint confirma que a aplicação responde; não verifica conexão com banco, autenticação ou outros serviços.

## Convenções atuais

- Formato de resposta: JSON.
- Prefixo de versão: `/api/v1`.
- O health check não requer autenticação.
- Não há outros endpoints implementados ou documentados como disponíveis.

## Manutenção do contrato

Ao implementar um endpoint:

1. Atualize `backend/openapi.yaml` junto com o código.
2. Inclua exemplos e respostas que correspondam ao comportamento real.
3. Atualize esta página se o fluxo exigir orientação para quem consome a API.

Não documente como disponível um endpoint que exista apenas como proposta ou história do MVP.
