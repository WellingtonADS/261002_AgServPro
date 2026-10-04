# Requisitos e critérios de aceite

**Status:** versão de trabalho baseada nas histórias e cenários discutidos; pontos pendentes dependem de validação.

## Histórias do MVP

| ID | Ator | Necessidade |
|---|---|---|
| US-01 | Prestador | Cadastrar clientes, contatos e locais para vincular solicitações corretamente. |
| US-02 | Prestador | Cadastrar equipamentos do cliente e consultar serviços anteriores por equipamento. |
| US-03 | Prestador | Registrar solicitações com canal, serviço pedido, local e problema relatado. |
| US-04 | Prestador | Fazer triagem e indicar se precisa de diagnóstico presencial antes do orçamento. |
| US-05 | Prestador | Preparar orçamento e registrar aprovação ou recusa, inclusive para serviços adicionais. |
| US-06 | Prestador | Agendar atendimento, informar responsável e evitar horários sobrepostos para o mesmo técnico. |
| US-07 | Técnico | Consultar a OS no celular e registrar diagnóstico, execução, materiais, fotos e observações. |
| US-08 | Prestador | Manter a OS pendente quando faltar aprovação, peça ou visita de retorno. |
| US-09 | Prestador | Encerrar a OS com resultado e confirmação de quem recebeu o serviço. |
| US-10 | Prestador | Consultar o histórico por cliente ou equipamento. |
| US-11 | Prestador | Registrar rapidamente pedidos recebidos pelo WhatsApp, mesmo com endereço ou equipamento pendentes. |
| CL-01 | Cliente | Consultar o orçamento pelo link e aprovar ou recusar sem criar conta. |
| CL-02 | Cliente | Receber data, faixa de horário e responsável; o envio pode ser manual no MVP. |
| CL-03 | Cliente | Receber resumo do serviço realizado e do resultado. |
| CL-04 | Cliente | Solicitar serviço pelo sistema e acompanhar sem falar com o prestador. **Fora do MVP.** |

## Critérios de aceite

| ID | Cenário a validar |
|---|---|
| TA-01 | Cadastrar cliente com nome e contato e vinculá-lo a uma solicitação. |
| TA-02 | Associar equipamento ao cliente e encontrá-lo no cadastro do cliente. |
| TA-03 | Registrar cliente, local, serviço e problema; iniciar como Solicitado. |
| TA-04 | Salvar solicitação incompleta como Aguardando informações, sem liberá-la para execução. |
| TA-05 | Marcar necessidade de diagnóstico presencial e registrá-lo separado do relato. |
| TA-06 | Compartilhar orçamento e registrar aprovação ou recusa; impedir serviço adicional sem nova aprovação. |
| TA-07 | Agendar e atribuir responsável; bloquear sobreposição para o mesmo técnico e explicar o conflito. |
| TA-08 | Abrir a OS no celular e iniciar atendimento, atualizando o estado. |
| TA-09 | Registrar diagnóstico, execução, materiais, observações e fotos ligados à OS. |
| TA-10 | Solicitar aprovação de serviço adicional antes de executá-lo. |
| TA-11 | Manter pendência de peça/retorno e vincular retorno à OS original. |
| TA-12 | Impedir conclusão sem resultado; ao concluir, gerar resumo para o cliente. |
| TA-13 | Consultar histórico por cliente ou equipamento, com data, serviço, estado e resultado. |
| TA-14 | Cancelar com motivo; retirar da agenda ativa e manter no histórico. |
| TA-15 | Registrar pelo WhatsApp usando telefone/nome e relato, inclusive texto colado, salvando sem endereço ou modelo do aparelho. |
| TA-16 | No link de acompanhamento, mostrar orçamento e permitir aprovação ou recusa. |
| TA-17 | No atendimento agendado, mostrar data e horário e oferecer “Falar pelo WhatsApp” e “Ver orçamento”. |
| TA-18 | Ao concluir, mostrar ao cliente resumo do serviço e resultado. |

## Observações

- O envio pelo WhatsApp é manual no MVP; não haverá importação automática de conversas.
- Estes são critérios de aceitação funcional, não uma solicitação de testes automatizados.
- Regras de transição e dados mínimos por estado estão em business-rules.md.
