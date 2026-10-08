# AgServPro — Gestão de Serviços de Manutenção

Sistema para organizar atendimentos de prestadores de serviços de climatização, do primeiro contato à conclusão da ordem de serviço.

## Sobre o projeto

O AgServPro ajuda profissionais autônomos e pequenas equipes de manutenção a registrar solicitações, preparar orçamentos, organizar a agenda e acompanhar a execução dos serviços.

### Diferenciais

- **Fluxo de atendimento integrado:** solicitação, orçamento, agenda e ordem de serviço no mesmo processo.
- **Operação multiempresa:** cada prestadora acessa os próprios dados.
- **Atendimento em campo:** histórico do serviço e acompanhamento pelo cliente.

## Situação atual

- **Produto:** escopo inicial do MVP definido.
- **Protótipo:** em validação final pelo cliente após ajustes de responsividade.
- **Arquitetura:** SaaS multiempresa e stack aprovados.
- **Aplicação:** base técnica iniciada; fluxos de negócio ainda não implementados.

A definição de autenticação, criação de prestadoras e aplicação técnica do isolamento multiempresa continua registrada em [Arquitetura](docs/architecture.md).

## Escopo do MVP

O MVP inclui registro rápido de solicitações recebidas pelo WhatsApp, cadastro de clientes e equipamentos, orçamento, agenda, ordem de serviço, execução em campo, acompanhamento do cliente e histórico.

Ficam fora desta etapa a gestão de PMOC, integração automática com WhatsApp ou Instagram, controle de estoque, financeiro completo, rastreamento de técnicos e portal com conta para o cliente solicitar serviços.

## Tecnologias

| Camada | Tecnologia |
|---|---|
| Backend | Java 21 e Spring Boot |
| API | REST/JSON em `/api/v1`, documentada com OpenAPI |
| Banco de dados | PostgreSQL com migrations Flyway |
| Frontend | Vue 3, TypeScript, Vite e Vue Router |
| Componentes visuais | PrimeVue com tema personalizado |
| Integração contínua | GitHub Actions |

A stack está aprovada; alguns componentes ainda não estão instalados ou implementados na base técnica.

## Documentação

Os documentos canônicos do projeto ficam em `docs/`.

- [Produto e escopo](docs/PRODUCT.md)
- [Requisitos e critérios de aceite](docs/requirements.md)
- [Regras de negócio](docs/business-rules.md)
- [Arquitetura e decisões pendentes](docs/architecture.md)
- [Design e protótipo](docs/design.md)
- [Instruções para desenvolvimento com IA](AGENTS.md)

## Estrutura do repositório

```text
.
├── backend/                 # API Java
├── frontend/                # Aplicação Vue
├── docs/                    # Documentação canônica
├── .github/workflows/       # Build e validação contínua
├── AGENTS.md                # Instruções de desenvolvimento com IA
└── README.md
```

## Requisitos para executar

- Java 21
- Maven
- Node.js 22 e npm

## Instalação e execução

Em um terminal, inicie a API:

```bash
mvn -f backend/pom.xml spring-boot:run
```

Em outro terminal, instale as dependências do frontend e inicie o servidor:

```bash
npm install --prefix frontend
npm run dev --prefix frontend
```

O frontend usa o proxy de desenvolvimento para encaminhar chamadas `/api` à API local em `http://localhost:8080`.

## Validação disponível

```bash
mvn -B -f backend/pom.xml verify
npm run build --prefix frontend
```

O workflow do GitHub Actions executa essas validações. A base atual ainda não possui testes automatizados de funcionalidades.

## Próximos passos

1. Concluir a validação do protótipo com o cliente.
2. Definir o fluxo de criação da prestadora, autenticação e convite de técnicos.
3. Definir e implementar o isolamento de dados por prestadora.
4. Iniciar a primeira fatia funcional conforme os requisitos aprovados.

## Contribuição

Abra uma issue com o objetivo e os critérios de aceite da alteração. Faça as mudanças em uma branch e envie um Pull Request para revisão. Consulte [AGENTS.md](AGENTS.md) e a documentação em `docs/` antes de alterar o projeto.

## Licença

A licença do projeto ainda não foi definida.

## Contato

- **Responsável:** Wellington Pinheiro — Ideia Code
- **E-mail:** [welltonuchoa@gmail.com](mailto:welltonuchoa@gmail.com)
- **GitHub:** [WellingtonADS](https://github.com/WellingtonADS)
