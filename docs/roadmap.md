# Roadmap do AgServPro

Este roadmap acompanha a evolução do MVP por marcos. Não define datas: cada etapa avança quando suas decisões e critérios estão prontos.

## Estado atual

- **Produto:** escopo e requisitos iniciais documentados.
- **Design:** protótipo responsivo em validação final pelo cliente.
- **Arquitetura:** SaaS multiempresa e stack aprovados.
- **Base técnica:** scaffold de API e frontend, contrato OpenAPI inicial e workflow de build no PR #1, ainda em rascunho.
- **Funcionalidades:** fluxos de negócio ainda não implementados.

## Marcos

| Marco | Situação | Condição para avançar |
|---|---|---|
| Base de produto, documentação e scaffold | Em andamento | Integrar a base técnica após revisão do PR; manter as fontes canônicas atualizadas |
| Aprovação do protótipo | Pendente | Cliente validar a versão responsiva e o fluxo navegável |
| Acesso e isolamento multiempresa | Pendente | Definir criação da prestadora, autenticação, convite de técnicos e como o backend aplicará o escopo da prestadora |
| Primeira fatia funcional | Pendente | Implementar cadastro do cliente e abertura rápida de solicitação conforme TA-01, TA-03, TA-04 e TA-15 |
| Fluxo operacional do MVP | Pendente | Implementar orçamento, agenda com bloqueio de conflito, OS, execução, pendências/retornos, conclusão e histórico |
| Validação para uso real | Pendente | Validar critérios de aceite, isolamento entre prestadoras, link do cliente, anexos e procedimento de publicação |

## Próximas ações

1. Concluir a validação do protótipo com o cliente.
2. Definir o fluxo de criação da prestadora, autenticação e convite de técnicos.
3. Definir o mecanismo de autorização e isolamento dos dados por prestadora antes de persistir dados operacionais.
4. Modelar os dados da primeira fatia e implementar o cadastro de cliente e a solicitação rápida.
5. Evoluir para orçamento, agenda e ordem de serviço conforme os requisitos e regras existentes.

## Pendências que afetam etapas posteriores

- Expiração, revogação e proteção do link de acompanhamento do cliente.
- Limites e retenção para fotos e anexos.
- Quantidade de técnicos no MVP.
- Confirmação de que conexão contínua atende ao uso em campo.
- Hospedagem, domínio e deploy.

As pendências técnicas detalhadas ficam em [architecture.md](architecture.md); o escopo e os critérios ficam em [PRODUCT.md](PRODUCT.md) e [requirements.md](requirements.md).

## Manutenção

Atualize este documento quando um marco mudar de situação ou quando uma decisão alterar a ordem do trabalho. Use issues e Pull Requests para acompanhar tarefas; não replique aqui as histórias e critérios de aceite.
