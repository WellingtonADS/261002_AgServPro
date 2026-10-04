# Instruções para agentes de desenvolvimento

## Antes de alterar

- Leia este arquivo e a documentação relacionada à tarefa.
- Inspecione a estrutura, o código, o banco e as integrações existentes antes de propor mudanças.
- Confirme a branch e o estado atual do repositório; preserve trabalho existente.
- Se uma decisão estiver marcada como pendente, não a trate como aprovada. Registre a dúvida ou proponha uma opção com justificativa.

## Regras do projeto

- Implemente somente itens do escopo aprovado em docs/PRODUCT.md e nos requisitos correspondentes.
- Não inclua funcionalidades fora do MVP, como PMOC, estoque, financeiro completo ou integração automática de mensagens, sem aprovação explícita.
- Reaproveite componentes, funções, modelos e integrações existentes. Evite duplicação e abstrações sem uso imediato.
- Preserve conexões existentes com banco e serviços. Não substitua nem recrie uma integração sem necessidade comprovada.
- Não altere esquema, dados ou permissões do banco sem entender o estado atual e registrar o impacto.
- Mantenha problema relatado pelo cliente separado do diagnóstico técnico.
- A agenda deve bloquear sobreposição para o mesmo técnico conforme docs/business-rules.md.
- Não marque OS como concluída se houver pendência operacional ou se faltarem os registros de conclusão definidos nos requisitos.
- Não invente decisões de produto, arquitetura, segurança ou interface. Sinalize o que ainda precisa de confirmação.

## Validação e entrega

- Não crie testes automatizados sem solicitação, exceto quando forem um gate já existente no projeto.
- Execute as validações relevantes já disponíveis e informe os comandos e resultados.
- Atualize a documentação canônica quando a implementação mudar uma regra ou decisão aprovada.
- Ao concluir, resuma o que mudou, arquivos afetados, validações realizadas e pendências.
