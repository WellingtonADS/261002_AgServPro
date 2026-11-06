# Roadmap do AgServPro

Este roadmap organiza o trabalho pelas etapas 6.1 a 6.10 do Development Manual. Não define datas: cada etapa avança quando suas decisões e critérios de saída estão prontos. As etapas iteram; uma descoberta pode reabrir uma decisão anterior.

## Estado atual

- **Produto e requisitos:** fontes iniciais registradas em `PRODUCT.md`, `requirements.md` e `business-rules.md`.
- **Design:** protótipo responsivo aguarda aprovação final do prestador; ver `design.md`.
- **Arquitetura:** SaaS multiempresa e stack aprovados; autenticação e mecanismo final de isolamento seguem pendentes.
- **Banco de dados:** modelo lógico proposto em `architecture/data-model.md`, ainda em revisão.
- **Base técnica:** scaffold e endpoint de saúde existentes; as funcionalidades de negócio ainda não foram implementadas.
- **Primeira entrega funcional:** cadastro de cliente e abertura rápida de solicitação; a OS só será criada após triagem e orçamento aprovado quando necessário.

## Etapas do ciclo de desenvolvimento

| Etapa do manual | Situação | Foco no AgServPro | Condição para avançar |
|---|---|---|---|
| **6.1 Preparação e alinhamento** | Em andamento | Confirmar objetivos, responsáveis, restrições e critérios para considerar o MVP pronto. | Escopo de entrega e critérios de aceite do MVP alinhados. |
| **6.2 Estratégia e requisitos** | Em andamento | Consolidar o escopo, as histórias e prioridades nos documentos existentes. | Primeira entrega funcional e itens fora do MVP claramente delimitados. |
| **6.3 Análise** | Em revisão | Fechar regras da solicitação, triagem, orçamento e OS; revisar o modelo lógico para manter esses registros separados. | Regras e entidades necessárias à primeira entrega sem ambiguidades bloqueadoras. |
| **6.4 Arquitetura e projeto técnico** | Parcialmente concluída | Stack e monólito modular aprovados; formalizar autenticação, provisionamento do diretor, permissões e isolamento multiempresa. | Acesso multiempresa e modelo de dados inicial definidos antes de persistir dados operacionais. |
| **6.5 Planejamento de execução** | Em andamento | Transformar a primeira fatia em tarefas pequenas com critérios de aceite e impacto conhecido. | Tarefas de cadastro de cliente, solicitação, triagem e conversão em OS prontas para execução. |
| **6.6 Migração e dados** | Não aplicável no momento | Não há migração de dados existentes prevista para o MVP. As migrations de esquema do banco serão tratadas na implementação. | Reavaliar se surgir uma fonte de dados legado a importar. |
| **6.7 Implementação** | Base técnica iniciada; negócio pendente | Implementar cadastro de cliente e fluxo de solicitação até a conversão elegível em OS, mantendo API e banco sincronizados. | Primeira fatia funcional concluída conforme seus critérios de aceite. |
| **6.8 Validação e qualidade** | Pendente | Conferir critérios funcionais, isolamento entre prestadoras, uso em celular e homologação com o prestador. | Fluxos críticos aceitos e problemas bloqueadores resolvidos. |
| **6.9 Go-Live** | Pendente | Definir hospedagem, domínio, configuração, publicação e procedimento de retorno. | Checklist de publicação e verificações pós-deploy definidos e executados. |
| **6.10 Operação e evolução** | Pendente | Preparar suporte, operação e priorização da evolução após o uso real. | Responsáveis e rotina de acompanhamento definidos para o MVP publicado. |

## Próxima sequência

1. Fechar autenticação e provisionamento: o admin SaaS cria o acesso inicial do diretor, que conclui a configuração da prestadora; decidir quando é criado o registro da prestadora e quem cria acessos de gerência/colaborador e suas permissões.
2. Detalhar estados da solicitação e da OS, incluindo arquivamento após recusa e criação de cópia independente.
3. Revisar o modelo lógico para refletir solicitação e OS separadas e confirmar o isolamento multiempresa.
4. Preparar tarefas para cadastro de cliente, solicitação, triagem, conversão e cópia de solicitação arquivada, usando os critérios existentes.
5. Implementar essa fatia, incluindo migrations necessárias, e validar que a OS só surge quando os critérios de conversão forem atendidos.
6. Seguir para orçamento, agenda, execução e histórico conforme os requisitos existentes.
7. Após a aprovação final do protótipo, registrar a referência visual aprovada e refiná-la durante a implementação da interface.

## Pendências para etapas posteriores

- Duração, reagendamento e histórico de horários.
- Dados obrigatórios para conclusão.
- Segurança, expiração e revogação do link do cliente.
- Limites e retenção de fotos e anexos.
- Quantidade de técnicos no MVP e confirmação do uso com conexão contínua.
- Hospedagem, domínio e deploy.

As regras e critérios funcionais ficam em [Regras de negócio](business-rules.md) e [Requisitos](requirements.md); as decisões técnicas ficam em [Arquitetura](architecture.md) e [Modelo lógico de dados](architecture/data-model.md). Este roadmap aponta a sequência e as condições de avanço, sem copiar o conteúdo dessas fontes.

## Manutenção

Atualize a situação quando uma etapa ou condição de saída mudar. Use issues e Pull Requests para acompanhar tarefas; não replique aqui histórias, critérios de aceite ou especificações já registradas.
