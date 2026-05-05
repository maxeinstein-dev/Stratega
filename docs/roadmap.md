# Stratega - Backend MVP & Evolução

Este documento consolida o planejamento técnico para o desenvolvimento da API Stratega, unindo o MVP básico com as recomendações de evolução (Diagnóstico Técnico de Nível Sênior).

---

## 🚀 Módulo 1 - MVP Básico (Concluído)

A fase inicial consistiu em estabelecer o **núcleo do Stratega**, garantindo que as operações básicas financeiras ocorressem com sucesso.

- Base do projeto Spring Boot estabelecida.
- Estrutura de Arquitetura Hexagonal (Controller → UseCase → Repository).
- Entidades criadas (`User`, `Wallet`, `Category`, `Transaction`).
- Funcionalidades de Criar Usuário, Carteiras, Categorias e Transações.
- Transferências internas entre carteiras.
- Lógica de atualização automática de saldo (`INCOME` soma, `EXPENSE` subtrai).

---

## 🔐 Módulo 2 - Segurança e Qualidade de Borda (Concluído)

Melhorias para garantir que a API seja consumível por um front-end moderno.

- **Autenticação JWT:** Endpoints de login e registro gerando tokens JWT.
- **Onboarding Automático:** Criação de uma carteira "Minha Carteira" no cadastro.
- **Isolamento de Dados (Ownership):** Filtro via JWT para que o usuário veja e altere apenas seus próprios dados (403 Forbidden).
- **Tratamento Global de Erros:** Padronização de erros (400 Bad Request, 403 Forbidden, 404 Not Found) via `GlobalExceptionHandler`.
- **Desacoplamento de Entidades:** Uso estrito de DTOs (`Request` e `Response`) nos Controllers, blindando o domínio.

---

## 👥 Módulo 3 - Social Finance / Despesas em Grupo (Concluído)

Módulo complexo de divisão de contas para viagens e rachadinhas.

- **Criação de Grupos:** Usuário logado vira "owner", adição livre de "members".
- **Tracking de Despesas:** Possibilidade de lançar gastos associados a grupos.
- **Estratégias de Divisão (`SplitType`):**
  - `UNIFORM`: Divisão igualitária automática.
  - `EXACT`: Lançamento de valores exatos por pessoa.
  - `PERCENTAGE`: Divisão baseada na cota percentual de cada um (deve somar 100%).
  - `SHARE`: Cotas de peso (Ex: 2 porções para João, 1 porção para Maria).
- **Liquidação Otimizada (`SuggestedTransfers`):** Algoritmo que calcula "quem deve para quem" de forma a reduzir o número de transferências necessárias.
- **Tracking Temporal:** Adicionado suporte à data nas transações de grupo.

---

## 🛠️ Módulo 4 - Flexibilidade e Operação Avançada (Concluído)

Garantindo liberdade e fluidez para a experiência do usuário.

- **Edição Avançada de Transações:**
  - Endpoint `PUT /api/transactions/{id}`.
  - Dupla Reversão em Transferências: Alterar o valor de uma transferência recalcula automaticamente o saldo da carteira de Origem e de Destino (mantendo integridade total).
  - Troca dinâmica de carteira devolvendo saldo para a carteira velha e debitando da nova.
  - Saldos Negativos permitidos por design para simular cartões de crédito.
- **Endpoints de Listagem Puros:** Criação dos GETters (`/api/wallets`, `/api/transactions`, `/api/groups`) já filtrando por usuário, preparando o terreno para dashboards.

---

## 🚀 Módulo 5 - Inteligência de Dados e Produção (Concluído)

Foco em prover insights financeiros e preparar a aplicação para um ambiente de produção real.

- **Dashboard Analítico:** Agrupamentos por categoria, balanço mensal, total de receitas e despesas.
- **Metas Financeiras (Budgets):** Definição de limites de gastos mensais por categoria com cálculo automático de percentual de consumo.
- **Lançamentos Recorrentes e Parcelamentos:** Suporte à criação em lote de transações futuras deduzindo o "saldo projetado" da carteira.
- **Exportação de Dados:** Geração de relatórios financeiros em formato `.csv`.
- **Importação de Dados:** Leitura de faturas e histórico bancário via extratos `.ofx`.
- **Ambiente de Produção (Docker + PostgreSQL):** Criação de `Dockerfile` otimizado, `docker-compose.yml` e `application-prod.properties` para deploy.

---

## 🔐 Módulo 6 - Integridade, Exclusões e Social Settlement (Concluído)

Foco em garantir a precisão matemática absoluta e facilitar acertos de contas.

- **Exclusão de Transações com Reconciliação:** Deletar uma transação reverte automaticamente o saldo da carteira.
- **Transferências de Unidade Única:** Refatoração de transferências para um agregado (`Transfer`) que permite exclusão atômica de ambas as carteiras.
- **Liquidação de Grupo com Injeção de Saldo:** Endpoint para liquidar dívidas de grupo que injeta o dinheiro diretamente em uma carteira do mundo real.
- **Importação Multi-formato:** Suporte a arquivos `.csv` e `.ofx` para facilitar a migração de outros apps.
- **Soft Delete de Carteiras:** Bloqueio de exclusão física para carteiras com histórico, permitindo apenas o arquivamento para preservar relatórios passados.
- **Segurança em Categorias:** Proteção contra edição de categorias globais e deleção de categorias em uso.

### Módulo 7: Globalização e Engajamento ✅
- **Globalização (Multi-Moedas):** Suporte a carteiras em USD/EUR com conversão automática via ExchangeRate-API.
- **Notificações Inteligentes:** Sistema de alertas in-app para orçamentos atingidos.
- **Relatórios Avançados:** Gráficos de tendência e comparativos mensais.

---

## 🔮 Backlog de Expansão (Implementações Futuras)

As próximas funcionalidades a serem implementadas no backend à medida que o projeto crescer:

1. **Anexos**
   - Upload de imagem/PDF para comprovantes e notas fiscais (Integração com AWS S3 ou armazenamento local).
2. **Conciliação Bancária**
   - Marcar transações como `PENDING` ou `SETTLED` para controle mais rígido.
3. **Integração Open Finance**
   - Conexão com APIs bancárias reais para leitura automática de extratos.
4. **Insights com IA**
   - Dicas automáticas de economia baseadas no padrão de consumo do usuário.
5. **Notificações Externas**
   - Integração com Push Notifications e Email.