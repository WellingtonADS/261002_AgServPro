# Regras de negócio

**Status:** regras confirmadas na definição do MVP; pontos ainda abertos estão no final.

## Solicitação, orçamento e ordem de serviço

- **RN-01 — Solicitação incompleta:** pode ser registrada sem endereço, modelo do equipamento ou disponibilidade. Permanece como solicitação pendente de informações e ainda não é uma OS.
- **RN-02 — Relato e diagnóstico:** o relato do cliente e o diagnóstico técnico são registros distintos. A triagem e o diagnóstico inicial ficam vinculados à solicitação.
- **RN-03 — Conversão em OS:** a solicitação só se converte em OS após a triagem. Se o serviço exigir orçamento, a conversão só ocorre depois da aprovação registrada; se não exigir orçamento, pode ocorrer após a triagem.
- **RN-04 — Serviço adicional:** serviço fora do orçamento aprovado exige aprovação adicional antes da execução.
- **RN-05 — Pendências:** falta de informação ou aprovação mantém a solicitação antes da OS; falta de peça ou visita de retorno mantém a OS aberta.
- **RN-06 — Retorno:** uma visita de retorno permanece vinculada à OS original, preservando o histórico.
- **RN-07 — Conclusão:** concluir a OS somente após registrar o serviço realizado, o resultado e a confirmação de quem recebeu.
- **RN-08 — Cancelamento:** registrar motivo ao cancelar solicitação ou OS. Uma OS cancelada deixa de ocupar horário ativo, mas permanece no histórico.
- **RN-09 — Histórico:** cada OS deve ser consultável por cliente e, quando houver equipamento associado, por equipamento; a solicitação de origem deve permanecer acessível.
- **RN-14 — Recusa e arquivamento:** quando o cliente recusar o orçamento, arquivar a solicitação e não criar uma OS. A solicitação arquivada permanece no histórico e pode ser usada como base para criar outra solicitação.
- **RN-15 — Cópia de solicitação:** copiar uma solicitação cria um novo registro independente, com novo identificador e estado inicial. A cópia não herda orçamento, decisão de aprovação/recusa, OS, agendamento ou execução; a solicitação arquivada original não é alterada.

## Agenda

- **RN-10 — Conflito:** bloquear intervalos sobrepostos para o mesmo técnico e informar o horário conflitante.
- **RN-11 — Duração:** cada agendamento precisa de início e término ou início e duração prevista para validar conflitos.
- **RN-12 — Cancelamento:** atendimento cancelado não ocupa mais o horário.
- **RN-13 — Retorno:** visita de retorno também passa pela validação de conflito.

## Estados propostos

- **Solicitação:** recebida, aguardando informações, em triagem, aguardando diagnóstico, aguardando orçamento, aguardando aprovação, convertida em OS, arquivada ou cancelada.
- **Orçamento:** rascunho, enviado, aprovado, recusado ou cancelado.
- **OS:** aberta, agendada, em atendimento, aguardando peça/retorno, concluída ou cancelada.
- **Agendamento:** agendado, confirmado, em atendimento, concluído ou cancelado.

Fluxo de referência: Solicitação → Triagem → (Informações pendentes | Diagnóstico | Orçamento, quando necessário) → Aprovação do orçamento, quando necessário → Conversão em OS → Agendamento → Atendimento → (Peça/retorno | Conclusão).

Uma solicitação sem orçamento necessário pode ser convertida após a triagem. Recusar um orçamento arquiva a solicitação e não cria uma OS. A solicitação arquivada pode ser copiada para iniciar uma nova solicitação independente.

## Pontos a confirmar

- Quais campos da solicitação devem vir preenchidos por padrão ao criar uma cópia.
- Campos e evidências obrigatórios para concluir uma OS.
- Como reagendamento registra motivo e histórico sem apagar o horário anterior.
- Validade e revogação do link privado de acompanhamento.
