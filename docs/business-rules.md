# Regras de negócio

**Status:** regras confirmadas na definição do MVP; pontos ainda abertos estão no final.

## Atendimento e ordem de serviço

- **RN-01 — Solicitação incompleta:** pode ser salva sem endereço, modelo do equipamento ou disponibilidade. Deve ficar pendente de informações e não pode ser considerada pronta para execução.
- **RN-02 — Relato e diagnóstico:** o relato do cliente e o diagnóstico técnico são registros distintos.
- **RN-03 — Aprovação de orçamento:** aprovação ou recusa precisa ficar registrada antes de iniciar o serviço orçado. Respostas por telefone ou pessoalmente podem ser registradas manualmente.
- **RN-04 — Serviço adicional:** serviço fora do orçamento exige aprovação adicional antes da execução.
- **RN-05 — Pendências:** falta de aprovação, peça ou retorno mantém o atendimento aberto.
- **RN-06 — Retorno:** uma visita de retorno permanece vinculada à OS original, preservando o histórico.
- **RN-07 — Conclusão:** concluir somente após registrar o serviço realizado, o resultado e a confirmação de quem recebeu.
- **RN-08 — Cancelamento:** registrar motivo; a OS cancelada deixa de ocupar horário ativo, mas permanece no histórico.
- **RN-09 — Histórico:** cada OS deve ser consultável por cliente e, quando houver equipamento associado, por equipamento.

## Agenda

- **RN-10 — Conflito:** bloquear intervalos sobrepostos para o mesmo técnico e informar o horário conflitante.
- **RN-11 — Duração:** cada agendamento precisa de início e término ou início e duração prevista para validar conflitos.
- **RN-12 — Cancelamento:** atendimento cancelado não ocupa mais o horário.
- **RN-13 — Retorno:** visita de retorno também passa pela validação de conflito.

## Estados propostos

Solicitado, Aguardando informações, Em triagem, Aguardando diagnóstico, Aguardando orçamento, Aguardando aprovação, Agendado, Em atendimento, Aguardando peça/retorno, Concluído e Cancelado. “Recusado” foi citado como estado; confirmar se representa a OS inteira ou somente a decisão do orçamento.

Fluxo de referência: Solicitação → Triagem → (Informações pendentes | Diagnóstico | Orçamento) → Aprovação → Agendamento → Atendimento → (Peça/retorno | Conclusão).

## Pontos a confirmar

- Se uma recusa encerra a OS ou apenas o orçamento.
- Campos e evidências obrigatórios para concluir uma OS.
- Como reagendamento registra motivo e histórico sem apagar o horário anterior.
- Validade e revogação do link privado de acompanhamento.
