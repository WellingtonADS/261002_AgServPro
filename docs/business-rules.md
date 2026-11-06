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
- **RN-15 — Cópia de solicitação:** usar uma solicitação arquivada como modelo abre um novo formulário preenchido com todo o conteúdo editável da solicitação, incluindo dados do cliente/serviço, informações de triagem e itens do orçamento recusado. O usuário pode editar antes de salvar. Ao salvar, cria-se uma solicitação independente, com novo identificador e estado inicial; itens de orçamento começam como novo rascunho, sem aprovação/recusa. OS, agendamentos, execuções, links e eventos históricos não são copiados; a solicitação arquivada original permanece inalterada.

## Acesso e configuração da prestadora

- **RN-16 — Acesso inicial:** o administrador do sistema SaaS provisiona o acesso inicial do diretor. O diretor conclui a configuração da prestadora.
- **RN-17 — Papéis:** administrador SaaS é um perfil da plataforma; diretor, gerência e colaborador são perfis de uma prestadora. O diretor cria os acessos de gerência e colaborador. As permissões detalhadas estão em proposta para validação.

## Proposta de permissões por perfil (validar)

| Perfil | Acesso proposto | Limites propostos |
|---|---|---|
| Administrador SaaS | Controle total do sistema: administra a plataforma, todas as prestadoras, diretores, usuários e operações, com acesso global. | Ações administrativas e acesso transversal a dados ficam auditados. É um papel da plataforma, não de uma prestadora. |
| Diretor | Controle completo da própria prestadora: configurações, dados e operações; cria, altera, desativa e atribui perfil a usuários de gerência e colaboradores. | Não pode conceder perfil de administrador SaaS nem acessar outra prestadora. |
| Gerência | Operação diária de toda a prestadora: clientes, equipamentos, solicitações, triagem, orçamentos, agenda, OS, atribuição de colaboradores e relatórios. | Não administra usuários, papéis, configuração ou estado da prestadora. |
| Colaborador (técnico) | Consulta agenda e OS atribuídas, além dos dados do cliente/equipamento necessários; registra diagnóstico, serviço realizado, materiais, fotos, observações, andamento e resultado. Pode registrar a conclusão do atendimento conforme os campos obrigatórios. | Não gerencia usuários, orçamentos, configuração, agenda geral ou OS não atribuídas; não altera atribuição de técnico. |

Para o MVP, os quatro perfis são fixos, sem editor de papéis personalizados. O administrador SaaS tem escopo global; diretor e gerência têm escopo da prestadora; colaborador tem escopo das OS atribuídas. A matriz detalhada permanece como proposta para validação antes da implementação de autorização.

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

- Campos e evidências obrigatórios para concluir uma OS.
- Como reagendamento registra motivo e histórico sem apagar o horário anterior.
- Validade e revogação do link privado de acompanhamento.
