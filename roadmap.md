# Diagnostico Tecnico - Projeto Stratega

## Objetivo

Este documento consolida uma revisao tecnica com foco de Tech Lead, priorizando arquitetura, boas praticas modernas, riscos e um plano de evolucao. Nao ha correcoes de codigo aqui, apenas orientacao.

## Resumo Executivo

- O projeto esta funcional para estudo e MVP.
- A estrutura em camadas basicas (controller/service/repository) esta presente e o build/teste de contexto passa.
- Para um padrao de producao, faltam protecoes importantes em contrato de API, tratamento de erros, dominio financeiro e testes.

## Achados Prioritarios

### Critico

1. Contrato de API acoplado a entidades JPA

- Controllers retornam entidades diretamente e, em um endpoint, tambem recebem entidade no body.
- Risco: vazamento de estrutura interna, quebra de contrato quando modelo mudar, acoplamento forte com persistencia.
- Direcao recomendada: separar API de dominio com DTOs de request/response e mapeadores.

2. Tratamento de erro inconsistente

- Uso de Optional na borda HTTP gera contrato ambiguo para recurso nao encontrado.
- IllegalArgumentException sem tratamento global pode virar erro 500 indevido.
- Direcao recomendada: padronizar erros com handler global e payload de erro consistente.

3. Uso de Double para valores monetarios

- Double nao e adequado para dinheiro por erro de precisao binaria.
- Direcao recomendada: usar tipo decimal exato para valores financeiros e definir politica de arredondamento.

### Alto

4. Validacao declarada, mas nao garantida na borda

- DTOs possuem anotacoes de validacao, mas falta garantir ativacao de validacao na entrada dos endpoints.
- Direcao recomendada: validar entrada no controller e padronizar resposta de violacao de regra.

5. CascadeType.PERSIST no lado ManyToOne

- Pode gerar persistencia implicita e duplicacao de categoria por efeito colateral.
- Direcao recomendada: revisar ownership do relacionamento e cascatas para evitar comportamento inesperado.

6. Cobertura de testes insuficiente

- Ha apenas teste de subida de contexto.
- Direcao recomendada: criar trilha minima de testes por camada (controller, service, repository) e cenarios de erro.

### Medio

7. Configuracao insegura para ambientes alem de local

- Console H2 habilitado e schema update no profile padrao.
- Direcao recomendada: separar configuracoes por ambiente (dev/test/prod) e endurecer defaults de producao.

8. Sinal de Open Session in View habilitado

- Pode ocultar problemas de carregamento lazy e consulta fora da camada correta.
- Direcao recomendada: definir postura explicita para o projeto e evitar dependencia acidental dessa feature.

9. Duplicacao de logica entre servicos

- Fluxos de criacao de receita e despesa repetem a mesma estrutura.
- Direcao recomendada: extrair politicas/regra comum para reduzir divergencia e custo de manutencao.

10. Anomalia no DTO de receita

- Existe classe interna que aparenta artefato acidental.
- Direcao recomendada: limpar modelagem de DTO para reduzir ruído e ambiguidade.

11. Listagens sem paginacao

- Uso direto de findAll em endpoints de listagem.
- Direcao recomendada: adotar paginacao, ordenacao e filtros para escalar.

### Baixo

12. Consistencia de naming REST

- Rotas no singular e sem estrategia de versionamento.
- Direcao recomendada: padronizar convencao (plural, versao de API) conforme contrato alvo.

## Maturidade Arquitetural (Score Atual)

Escala: 0 (inicial) a 5 (excelente)

- Separacao de camadas: 3/5
- Contrato de API e versionamento: 2/5
- Modelagem de dominio financeiro: 1/5
- Tratamento de erro: 2/5
- Validacao de entrada: 2/5
- Persistencia e relacionamentos: 2/5
- Testes automatizados: 1/5
- Observabilidade e operacao: 1/5
- Seguranca baseline: 1/5

## Roadmap Sugerido (Impacto x Esforco)

### Fase 1 (1 semana) - Fundacao da API

- Padronizar modelo de erro da API.
- Fortalecer validacao de entrada.
- Definir contrato de response desacoplado de entidade.
- Introduzir paginacao nas listagens.

### Fase 2 (1-2 semanas) - Dominio e Persistencia

- Migrar valores monetarios para tipo decimal exato.
- Revisar cascatas e integridade de relacionamentos.
- Consolidar regra compartilhada entre servicos.

### Fase 3 (1 semana) - Qualidade e Operacao

- Montar matriz de testes (unitario, web, repositorio).
- Separar configuracoes por ambiente.
- Preparar observabilidade minima (logs estruturados e rastreabilidade).

## Checklist de Prontidao para Producao

- Contrato de API estavel e versionado
- Erros padronizados com codigos previsiveis
- Dinheiro com precisao decimal e regras de arredondamento
- Validacao robusta de entrada
- Cobertura minima de testes por camada
- Configuracao separada por ambiente
- Paginacao e filtros nas listagens
- Monitoramento/logs basicos

## Decisoes Estrategicas Confirmadas

- Fase atual: projeto de estudo.
- Direcao de medio prazo: expor API para frontend publico.
- Auditoria detalhada (historico completo de alteracoes): nao obrigatoria neste momento por ser uso pessoal.
- Seguranca: obrigatoria autenticacao e autorizacao, com cada pessoa acessando somente seus proprios dados.
- Consumo: frontend separado consumindo API publica.

## Ordem Recomendada de Correcao e Implementacao

### Etapa 1 - Base de API Confiavel

1. Desacoplar contrato HTTP das entidades (DTO de entrada/saida).
2. Padronizar tratamento de erro com payload consistente.
3. Ativar e consolidar validacoes de entrada em todos os endpoints.
4. Padronizar rotas REST (plural + versao da API).

### Etapa 2 - Seguranca e Multiusuario

5. Implementar autenticacao (JWT ou sessao) e autorizacao por usuario.
6. Introduzir ownership de dados (todo registro vinculado ao dono).
7. Garantir isolamento: usuario so enxerga/altera dados proprios.
8. Endurecer configuracao por ambiente (dev/test/prod).

### Etapa 3 - Dominio Financeiro Confiavel

9. Migrar valores monetarios para tipo decimal exato.
10. Revisar cascatas e integridade de relacionamentos.
11. Eliminar duplicacao de regras de negocio entre servicos.
12. Adicionar paginacao, ordenacao e filtros nas listagens.

### Etapa 4 - Qualidade e Evolucao

13. Criar suite de testes por camada (controller/service/repository).
14. Definir observabilidade minima (logs estruturados, correlacao de requisicao).
15. Publicar documentacao de API para o frontend (contratos e exemplos).

## Backlog de Expansao (Implementacoes Futuras)

1. Rateio de despesas

- Criar entidade/classe de rateio para dividir uma despesa entre uma ou mais pessoas, com percentual ou valor fixo por participante.

2. Carteiras/contas financeiras

- Permitir separacao por conta (dinheiro, banco, cartao) para melhor visao de saldo.

3. Lancamentos recorrentes

- Regras de repeticao para receitas/despesas mensais, semanais e anuais.

4. Metas financeiras

- Metas por categoria e periodo, com acompanhamento de progresso.

5. Orcamento mensal por categoria

- Limites por categoria com alerta de estouro de teto.

6. Conciliação de transacoes

- Marcar lancamentos previstos como realizados e reconciliar divergencias.

7. Centro de notificacoes

- Alertas de vencimento, excesso de gasto e recebimentos pendentes.

8. Dashboard analitico

- Indicadores de fluxo de caixa, top categorias e tendencia mensal.

9. Importacao/exportacao de dados

- CSV/Excel para migracao, backup e analise externa.

10. Anexos por lancamento

- Vincular comprovantes (imagem/PDF) a receitas e despesas.

11. Tags e filtros avancados

- Classificacao transversal para analise por contexto (viagem, casa, trabalho).

12. Planejamento de cenarios

- Simulador de impacto financeiro com cenarios otimista, realista e conservador.

## Roteiro de Prioridade para Expor Frontend Publico

- Primeiro: estabilidade de contrato (DTOs, erros, validacao).
- Segundo: seguranca e isolamento de dados por usuario.
- Terceiro: confiabilidade financeira (precisao monetaria + regras de dominio).
- Quarto: experiencia de produto (dashboard, metas, rateio, alertas).

## Plano de Sprints (Execucao Recomendada)

Premissas deste plano:

- Sprint semanal.
- Entregas incrementais com API sempre executavel ao final de cada sprint.
- Esforco relativo: P (pequeno), M (medio), G (grande).

### Sprint 1 - Fundacao de Contrato e Erros

Objetivo: estabilizar contrato da API para consumo por frontend separado.

Itens:

1. Definir DTOs de request/response para categoria, receita e despesa. Esforco: G.
2. Padronizar respostas de erro da API (modelo unico). Esforco: M.
3. Padronizar codigos HTTP para cenarios comuns (400, 401, 403, 404, 409, 422, 500). Esforco: M.
4. Versionar rotas da API e padronizar naming REST no plural. Esforco: M.

Criterios de aceite:

1. Nenhum endpoint retorna entidade JPA diretamente.
2. Erros da API seguem um formato unico e documentado.
3. Recurso inexistente retorna 404 com mensagem clara.
4. Build e testes existentes passam sem regressao.

### Sprint 2 - Validacao de Entrada e Qualidade de Borda

Objetivo: impedir entrada invalida e melhorar previsibilidade para o frontend.

Itens:

1. Ativar validacao em todos os endpoints de escrita (POST/PUT/PATCH). Esforco: M.
2. Definir mensagens padrao para violacao de regra de validacao. Esforco: P.
3. Adicionar testes de controller para sucesso e erro de validacao. Esforco: G.
4. Publicar exemplos de payload valido/invalido para cada endpoint principal. Esforco: M.

Criterios de aceite:

1. Payload invalido retorna 400 com lista de campos invalidos.
2. Regras de nome obrigatorio e valor nao negativo sao validadas na borda.
3. Existe cobertura de testes de controller para pelo menos 1 caso de sucesso e 1 de erro por recurso principal.

### Sprint 3 - Seguranca Base e Multiusuario

Objetivo: preparar API para uso por pessoas diferentes com isolamento de dados.

Itens:

1. Implementar autenticacao (JWT) com endpoint de login. Esforco: G.
2. Criar modelo de usuario e vincular ownership dos dados financeiros. Esforco: G.
3. Aplicar autorizacao para impedir acesso a dados de outro usuario. Esforco: G.
4. Criar testes de seguranca para 401/403 e isolamento de ownership. Esforco: M.

Criterios de aceite:

1. Endpoint protegido sem token retorna 401.
2. Usuario autenticado nao acessa recurso de outro usuario (403 ou 404, conforme estrategia).
3. Toda consulta de receita/despesa/categoria e filtrada por dono do dado.

### Sprint 4 - Dominio Financeiro Confiavel

Objetivo: corrigir fundamentos do dominio para evitar inconsistencias de dinheiro e relacionamento.

Itens:

1. Migrar valores financeiros para tipo decimal exato. Esforco: G.
2. Definir politica de arredondamento e escala monetaria. Esforco: M.
3. Revisar relacionamentos e remover cascatas com risco de persistencia implicita. Esforco: M.
4. Eliminar duplicacao de regra entre servicos de receita e despesa. Esforco: M.

Criterios de aceite:

1. Operacoes financeiras nao usam Double no dominio principal.
2. Arredondamento e escala seguem regra unica e testada.
3. Criacao de receita/despesa nao gera categoria duplicada por efeito colateral.

### Sprint 5 - Escalabilidade Basica e Operacao

Objetivo: melhorar desempenho funcional para uso real e facilitar manutencao.

Itens:

1. Adicionar paginacao e ordenacao em listagens. Esforco: M.
2. Adicionar filtros essenciais (periodo, categoria, faixa de valor). Esforco: M.
3. Separar configuracoes por ambiente (dev/test/prod). Esforco: M.
4. Definir logging estruturado minimo para diagnostico de requisicoes. Esforco: M.

Criterios de aceite:

1. Listagens suportam page, size e sort.
2. Endpoints principais aceitam filtros sem quebrar contrato.
3. Configuracao de producao nao expoe console H2.
4. Logs permitem rastrear erro com contexto minimo de requisicao.

### Sprint 6 - Documentacao e Prontidao de Integracao Frontend

Objetivo: facilitar consumo da API por frontend publico com menor atrito.

Itens:

1. Publicar documentacao de API com exemplos reais de request/response. Esforco: M.
2. Definir convencoes de erro e codigos de negocio para o frontend. Esforco: P.
3. Criar colecao de testes de contrato (manual ou automatizada) para endpoints criticos. Esforco: M.
4. Revisar checklist de prontidao de producao e fechar pendencias. Esforco: M.

Criterios de aceite:

1. Frontend consegue implementar fluxo de login, listagem e cadastro sem dependencia de esclarecimento verbal.
2. Contrato de erro esta documentado e coberto por exemplos.
3. Existe checklist final assinado para liberar proxima etapa de expansao.

## Backlog Pos-Sprint (Expansao de Produto)

Ordem sugerida apos estabilizacao da base:

1. Rateio de despesas entre pessoas.
2. Lancamentos recorrentes.
3. Orcamento mensal por categoria.
4. Metas financeiras.
5. Dashboard analitico.
6. Notificacoes de vencimento e limite.
7. Conciliação de transacoes.
8. Carteiras/contas financeiras.
9. Tags e filtros avancados.
10. Importacao/exportacao CSV/Excel.
11. Anexos por lancamento.
12. Planejamento de cenarios.

## Definicao de Pronto (DoD)

Para considerar qualquer item concluido:

1. Build local passando.
2. Testes da funcionalidade adicionados e aprovados.
3. Contrato de API atualizado na documentacao.
4. Sem regressao dos endpoints existentes.
5. Seguranca e ownership respeitados quando aplicavel.
