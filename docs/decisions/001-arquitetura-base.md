# ADR 001 — Arquitetura base do AgServPro

- **Status:** aprovado
- **Data:** 2026-10-08
- **Responsável pela decisão:** Wellington Pinheiro

## Contexto

O AgServPro será um SaaS para profissionais autônomos e pequenas equipes de manutenção. O MVP precisa atender prestadoras diferentes, organizar o fluxo de solicitação a ordem de serviço e oferecer uma interface web adequada ao uso em computador e celular.

A arquitetura precisa permitir evolução simples no início, manter um contrato claro entre interface e backend e impedir que uma prestadora acesse dados de outra.

## Decisões

1. O produto será **SaaS multiempresa desde o MVP**.
2. O projeto usará um **monólito modular** no backend.
3. O backend será desenvolvido em **Java 21 com Spring Boot**.
4. A interface será uma aplicação **Vue 3 com TypeScript, Vite e Vue Router**.
5. A comunicação entre interface e backend usará **API REST/JSON versionada em `/api/v1`**, documentada com OpenAPI.
6. O banco será **PostgreSQL**, com alterações de esquema versionadas pelo Flyway.
7. A interface usará **PrimeVue com tema personalizado**.
8. Dados operacionais deverão pertencer a uma prestadora. O backend deverá determinar o escopo da prestadora a partir da identidade autenticada; um identificador enviado pelo navegador não será suficiente para autorizar acesso.
9. O MVP será **online**. Modo offline não foi aprovado.
10. O protótipo HTML, após aprovação do cliente, será a referência visual da implementação.

## Alternativas consideradas

- **Serviços independentes:** não escolhidos para o MVP; aumentariam a complexidade de desenvolvimento e operação sem uma necessidade atual.
- **Frontend com renderização no servidor:** não escolhido; o produto começa como painel operacional autenticado e não tem necessidade definida que justifique SSR.
- **Modo offline:** não aprovado para o MVP; a necessidade de conexão contínua em campo ainda será confirmada.

## Consequências

- A API e a interface podem evoluir separadamente dentro do mesmo produto e repositório.
- As regras de negócio e autorização devem permanecer no backend.
- Dados de cada prestadora precisam permanecer isolados em todas as operações.
- O protótipo ainda não deve ser tratado como baseline visual final enquanto aguarda aprovação.
- Banco, autenticação, isolamento e interface visual serão implementados em etapas; a aprovação da stack não significa que esses recursos já existam na base técnica.

## Decisões ainda pendentes

Este ADR não define:

- fluxo de criação da prestadora, autenticação e convite de técnicos;
- mecanismo técnico de aplicação e validação do isolamento multiempresa;
- perfis e permissões finais;
- proteção, expiração e revogação do link do cliente;
- armazenamento, limites e retenção de anexos;
- quantidade de técnicos no MVP;
- hospedagem, domínio e deploy.

As pendências são acompanhadas em [architecture.md](../architecture.md) e no [roadmap](../roadmap.md).
