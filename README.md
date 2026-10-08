# AgServPro — Gestão de Serviços de Manutenção

Sistema para organizar atendimentos de prestadores de serviços de climatização, desde o pedido recebido até a conclusão da ordem de serviço.

## Situação

- Fase: definição do MVP e validação final do protótipo.
- Repositório: documentação e base técnica inicial; fluxos de negócio ainda não implementados.
- Protótipo: aguarda aprovação final após correção da responsividade.
- SaaS multiempresa e stack do MVP: aprovados.

## Escopo inicial

O MVP cobre cadastro rápido de solicitações recebidas pelo WhatsApp, clientes e equipamentos, orçamento, agenda, ordem de serviço, execução em campo, acompanhamento do cliente e histórico.

Ficam fora do MVP: gestão de PMOC, integração automática com WhatsApp ou Instagram, controle de estoque, financeiro completo, rastreamento de técnicos e portal com conta para o cliente solicitar serviços.

## Documentação

- [Produto e escopo](docs/PRODUCT.md)
- [Requisitos e critérios de aceite](docs/requirements.md)
- [Regras de negócio](docs/business-rules.md)
- [Arquitetura e decisões pendentes](docs/architecture.md)
- [Design e protótipo](docs/design.md)
- [Instruções para desenvolvimento com IA](AGENTS.md)

## Base técnica

- `backend/`: API Java 21 com Spring Boot.
- `frontend/`: Vue 3 com TypeScript e Vite.
- `backend/openapi.yaml`: contrato inicial REST.
- `.github/workflows/build.yml`: compilação do backend e build do frontend.

Os fluxos de autenticação, cadastro e operação ainda serão implementados conforme as decisões pendentes em [arquitetura](docs/architecture.md).
