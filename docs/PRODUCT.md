# Produto — AgServPro

**Status:** escopo inicial aprovado como direção do MVP; decisões pendentes estão identificadas abaixo.

## Problema

Pedidos chegam por canais como WhatsApp e podem ficar espalhados em conversas. O prestador precisa registrar o essencial rapidamente e acompanhar cada serviço sem perder informações entre orçamento, agenda, execução e retorno.

## Público inicial

Prestadores autônomos e pequenas operações de instalação, limpeza e manutenção de ar-condicionado. O produto deve funcionar bem no celular para o uso em campo e no atendimento diário.

## Resultado esperado

Permitir que o prestador acompanhe o atendimento desde a solicitação, passando por triagem e orçamento quando necessário, até a ordem de serviço e o encerramento. A solicitação e a OS são registros distintos; a OS só é criada após a triagem e, quando houver orçamento, sua aprovação.

## Escopo do MVP

- Cadastro de clientes, contatos e locais de atendimento.
- Cadastro simples de equipamentos.
- Cadastro rápido de solicitações recebidas pelo WhatsApp, mesmo com dados pendentes.
- Triagem da solicitação e distinção entre relato do cliente e diagnóstico técnico.
- Orçamento vinculado à solicitação, com registro de aprovação ou recusa quando necessário.
- Arquivamento de solicitações com orçamento recusado e possibilidade de abrir uma cópia completa e editável como base para uma nova solicitação.
- Conversão da solicitação em OS somente após triagem e, quando exigido, aprovação do orçamento.
- Agenda e atribuição de responsável, bloqueando conflito de horário para o mesmo técnico.
- Ordem de serviço consultável e atualizável no celular.
- Registro de execução, materiais, observações, fotos e resultado.
- Tratamento de serviço adicional, peça pendente e retorno vinculado à OS original.
- Conclusão com confirmação e resumo compartilhável com o cliente.
- Acompanhamento do serviço pelo cliente por link, sem exigir conta.
- Histórico por cliente e equipamento.

## Fora do MVP

- Gestão de PMOC.
- Integração automática com WhatsApp ou Instagram.
- Controle de estoque e financeiro completo.
- Rastreamento de técnicos.
- Portal e conta própria para o cliente solicitar ou acompanhar serviços.
- Contratos preventivos e automações avançadas.

## Premissas e pendências

- SaaS multiempresa aprovado para o MVP. O mecanismo técnico de isolamento está pendente.
- O MVP foi pensado para uso com internet; operação offline não foi aprovada.
- Atendimento comercial sem exigência de PMOC ainda precisa ser delimitado.
- O protótipo HTML corrigido aguarda aprovação final de responsividade.
- Cobrança de assinatura não faz parte do escopo inicial.
