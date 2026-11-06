# Requisitos e critérios de aceite

**Status:** versão de trabalho baseada nas histórias e cenários discutidos; pontos pendentes dependem de validação.

## Histórias do MVP

| ID | Ator | Necessidade |
|---|---|---|
| US-01 | Prestador | Cadastrar clientes, contatos e locais para vincular solicitações corretamente. |
| US-02 | Prestador | Cadastrar equipamentos do cliente e consultar serviços anteriores por equipamento. |
| US-03 | Prestador | Registrar solicitações com canal, serviço pedido, local e problema relatado. |
| US-04 | Prestador | Fazer triagem da solicitação e indicar se precisa de diagnóstico presencial antes do orçamento ou da conversão em OS. |
| US-05 | Prestador | Preparar orçamento vinculado à solicitação e registrar aprovação ou recusa antes de convertê-la em OS, quando orçamento for necessário. |
| US-06 | Prestador | Agendar atendimento, informar responsável e evitar horários sobrepostos para o mesmo técnico. |
| US-07 | Técnico | Consultar a OS criada após triagem e registrar execução, materiais, fotos e observações. |
| US-08 | Prestador | Manter a solicitação sem OS enquanto faltar aprovação; manter a OS aberta quando faltar peça ou visita de retorno. |
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
| TA-03 | Registrar uma solicitação com cliente, local, serviço e problema; iniciar como Recebida. |
| TA-04 | Salvar solicitação incompleta como Aguardando informações; não criar OS nem liberar execução. |
| TA-05 | Marcar na triagem a necessidade de diagnóstico presencial e registrá-lo separado do relato do cliente. |
| TA-06 | Vincular orçamento à solicitação e registrar aprovação ou recusa; impedir conversão em OS sem aprovação quando orçamento for necessário e impedir serviço adicional sem nova aprovação. |
| TA-07 | Agendar e atribuir responsável; bloquear sobreposição para o mesmo técnico e explicar o conflito. |
| TA-08 | Abrir no celular uma OS já criada após a triagem e as aprovações necessárias; iniciar atendimento, atualizando o estado. |
| TA-09 | Registrar diagnóstico, execução, materiais, observações e fotos ligados à OS. |
| TA-10 | Solicitar aprovação de serviço adicional antes de executá-lo. |
| TA-11 | Manter pendência de peça/retorno e vincular retorno à OS original. |
| TA-12 | Impedir conclusão sem resultado; ao concluir, gerar resumo para o cliente. |
| TA-13 | Consultar histórico por cliente ou equipamento, com data, serviço, estado e resultado. |
| TA-14 | Cancelar com motivo; retirar da agenda ativa e manter no histórico. |
| TA-15 | Registrar pelo WhatsApp uma solicitação usando telefone/nome e relato, inclusive texto colado, mesmo sem endereço ou modelo do aparelho; não criar OS nessa etapa. |
| TA-16 | No link de acompanhamento, mostrar orçamento e permitir aprovação ou recusa. |
| TA-17 | No atendimento agendado, mostrar data e horário e oferecer “Falar pelo WhatsApp” e “Ver orçamento”. |
| TA-18 | Ao concluir a OS, mostrar ao cliente resumo do serviço e resultado. |
| TA-19 | Criar OS vinculada à solicitação somente após triagem concluída e, quando necessário, aprovação do orçamento; permitir conversão sem orçamento quando a triagem indicar que ele não é necessário. |

## Observações

- O envio pelo WhatsApp é manual no MVP; não haverá importação automática de conversas.
- Estes são critérios de aceitação funcional, não uma solicitação de testes automatizados.
- Regras de transição e dados mínimos por estado estão em business-rules.md.
