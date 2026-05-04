# 📄 Especificação Técnica para Desenvolvimento Frontend - Stratega API
 
Este documento descreve as funcionalidades, regras de negócio e endpoints da API **Stratega**, fornecendo todo o contexto necessário para a construção de uma interface frontend moderna e funcional.
 
---
 
## 🚀 Visão Geral do Sistema
O **Stratega** é um sistema de gestão financeira pessoal e em grupo. Ele permite que usuários controlem suas próprias carteiras e participem de grupos de despesas compartilhadas com algoritmos de liquidação de dívidas.
 
---
 
## 🔐 Autenticação e Segurança
A API utiliza **JWT (JSON Web Token)** para segurança.
- **Header:** `Authorization: Bearer <TOKEN>`
- **Identidade Automática:** A API identifica o usuário logado através do token. Não é necessário enviar IDs de usuário manualmente nos headers ou no corpo das requisições principais.
- **Endpoints Públicos:** `/api/auth/register` e `/api/auth/login`.
- **Endpoints Protegidos:** Todos os demais requerem o token.
 
---
 
## 📂 Módulos e Endpoints
 
### 1. Autenticação (`/api/auth`)
Gerencia o acesso dos usuários.
 
*   **POST `/api/auth/register`**
    *   **Body:** `{ "name": "...", "email": "...", "password": "..." }`
    *   **Response:** `201 Created` - `{ "id": "...", "name": "...", "email": "..." }`
    *   **Nota:** Ao registrar, uma carteira padrão chamada "Minha Carteira" é criada automaticamente para o usuário.
*   **POST `/api/auth/login`**
    *   **Body:** `{ "email": "...", "password": "..." }`
    *   **Response:** `200 OK` - `{ "accessToken": "..." }`
 
---
 
### 2. Carteiras (`/api/wallets`)
Representam contas bancárias, dinheiro em espécie ou cartões de crédito.
 
*   **POST `/api/wallets`**
    *   Cria uma nova carteira para o usuário logado.
    *   **Body:** `{ "name": "...", "initialBalance": 0.0 }`
    *   **Response:** `201 Created` - `{ "id": "...", "name": "...", "balance": 0.0, "userId": "..." }`
*   **GET `/api/wallets`**
    *   Retorna a lista de carteiras do usuário logado.
    *   **Response:** `200 OK` - `[ { "id": "...", "name": "...", "balance": 0.0, "userId": "..." } ]`
 
---
 
### 3. Transações (`/api/transactions`)
Movimentações financeiras individuais.
 
*   **POST `/api/transactions`**
    *   Cria uma receita (INCOME) ou despesa (EXPENSE). Atualiza automaticamente o saldo da carteira vinculada.
    *   **Body:** 
        ```json
        {
          "description": "Almoço",
          "amount": 25.50,
          "date": "2026-05-04T15:00:00",
          "type": "EXPENSE",
          "walletId": "<UUID>",
          "categoryId": "<UUID> (Opcional)"
        }
        ```
*   **GET `/api/transactions`**
    *   Retorna o histórico de transações do usuário logado (ordenado por data decrescente).
    *   **Response:** `200 OK` - `[ { "id": "...", "description": "Almoço", "amount": 25.50, "date": "...", "type": "EXPENSE", ... } ]`
*   **POST `/api/transactions/transfer`**
    *   Realiza transferência entre duas carteiras do usuário logado.
    *   **Body:**
        ```json
        {
          "originWalletId": "<UUID>",
          "destinationWalletId": "<UUID>",
          "amount": 100.0,
          "description": "Reserva",
          "date": "2026-05-04T15:00:00"
        }
        ```
*   **PUT `/api/transactions/{id}`**
    *   Edita uma transação existente. Permite alterar `walletId`, `categoryId`, `amount`, `date` e `description`. O `type` é imutável.
    *   **Body:**
        ```json
        {
          "description": "Novo Almoço",
          "amount": 30.00,
          "date": "2026-05-04T15:00:00",
          "walletId": "<UUID>",
          "categoryId": "<UUID> (Opcional)"
        }
        ```
 
---
 
### 4. Despesas em Grupo (`/api/groups`)
Módulo social para dividir contas entre amigos ou familiares.
 
*   **POST `/api/groups`**
    *   Cria um grupo. O usuário logado é definido automaticamente como o dono (`owner`).
    *   **Body:** `{ "name": "Viagem", "memberNames": ["Alice", "Bob"] }`
*   **GET `/api/groups`**
    *   Retorna a lista de grupos em que o usuário logado é o dono ou um membro.
    *   **Response:** `200 OK` - `[ { "id": "...", "name": "Viagem", "ownerId": "...", "members": [...] } ]`
*   **POST `/api/groups/{groupId}/expenses`**
    *   Adiciona uma despesa ao grupo usando uma estratégia de divisão.
    *   **Estratégias (`splitType`):**
        1.  `UNIFORM`: Divide igualmente entre todos os membros.
        2.  `EXACT`: Cada membro paga um valor fixo.
        3.  `PERCENTAGE`: Divisão por porcentagem (deve somar 100%).
        4.  `SHARE`: Divisão por cotas (ex: Pessoa A tem 2 partes, Pessoa B tem 1).
    *   **Body:**
        ```json
        {
          "description": "Jantar",
          "amount": 150.0,
          "paidByMemberId": "<UUID>",
          "date": "2026-05-04T19:30:00",
          "splitType": "UNIFORM",
          "splitValues": {} 
        }
        ```
*   **GET `/api/groups/{groupId}/balances`**
    *   Retorna o balanço de cada membro e as **transferências sugeridas**.
 
---
 
## 💡 Notas para o Frontend
1.  **Imutabilidade:** O ID de um grupo ou transação nunca muda.
2.  **Membros Virtuais:** Membros de grupo podem não ter um `userId`. O frontend deve lidar com membros que são apenas strings.
3.  **Saldo:** A API permite saldos negativos em carteiras.
4.  **Autenticação**: O token deve ser enviado no header `Authorization` em todas as rotas protegidas.
 
---
 
## 🎨 Sugestão de UI (User Experience)
*   **Dashboard:** Gráfico de receitas vs despesas e saldo total das carteiras.
*   **Módulo de Grupos:** Lista de grupos, indicador visual de "Você deve" ou "Te devem".
*   **Criação de Despesa:** Interface dinâmica baseada no `splitType` selecionado.
