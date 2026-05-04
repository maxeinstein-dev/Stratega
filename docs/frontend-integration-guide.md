# 📄 Especificação Técnica para Desenvolvimento Frontend - Stratega API

Este documento descreve as funcionalidades, regras de negócio e endpoints da API **Stratega**, fornecendo todo o contexto necessário para a construção de uma interface frontend moderna e funcional.

---

## 🚀 Visão Geral do Sistema
O **Stratega** é um sistema de gestão financeira pessoal e em grupo. Ele permite que usuários controlem suas próprias carteiras e participem de grupos de despesas compartilhadas com algoritmos de liquidação de dívidas.

---

## 🔐 Autenticação e Segurança
A API utiliza **JWT (JSON Web Token)** para segurança.
- **Header:** `Authorization: Bearer <TOKEN>`
- **Endpoints Públicos:** `/api/auth/register` e `/api/auth/login`.
- **Endpoints Protegidos:** Todos os demais requerem o token.

---

## 📂 Módulos e Endpoints

### 1. Autenticação (`/api/auth`)
Gerencia o acesso dos usuários.

*   **POST `/api/auth/register`**
    *   **Body:** `{ "name": "...", "email": "...", "password": "..." }`
    *   **Response:** `201 Created` - `{ "id": "...", "name": "...", "email": "..." }`
*   **POST `/api/auth/login`**
    *   **Body:** `{ "email": "...", "password": "..." }`
    *   **Response:** `200 OK` - `{ "token": "..." }`

---

### 2. Carteiras (`/api/wallets`)
Representam contas bancárias, dinheiro em espécie ou cartões de crédito.

*   **POST `/api/wallets`**
    *   **Body:** `{ "name": "...", "initialBalance": 0.0, "userId": "..." }`
    *   **Response:** `201 Created` - `{ "id": "...", "name": "...", "balance": 0.0, "userId": "..." }`

---

### 3. Transações (`/api/transactions`)
Movimentações financeiras individuais.

*   **POST `/api/transactions`**
    *   Cria uma receita (INCOME) ou despesa (EXPENSE). Atualiza automaticamente o saldo da carteira vinculada.
    *   **Headers:** `X-User-Id: <UUID>` (Identificação do autor)
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
*   **POST `/api/transactions/transfer`**
    *   Realiza transferência entre duas carteiras do mesmo usuário.
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

---

### 4. Despesas em Grupo (`/api/groups`)
Módulo social para dividir contas entre amigos ou familiares.

*   **POST `/api/groups`**
    *   Cria um grupo e define os membros (podem ser usuários cadastrados ou "membros virtuais" apenas com nome).
    *   **Body:** `{ "name": "Viagem", "ownerId": "<UUID>", "members": ["Alice", "Bob"] }`
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
          "splitType": "UNIFORM",
          "splitValues": {} 
        }
        ```
*   **GET `/api/groups/{groupId}/balances`**
    *   Retorna o balanço de cada membro e as **transferências sugeridas** (quem deve pagar quem) para zerar as dívidas com o menor número de transações.
    *   **Response:**
        ```json
        {
          "groupId": "...",
          "groupName": "...",
          "memberBalances": { "<memberId>": 50.0, "<memberId>": -50.0 },
          "suggestedTransfers": [
            { "from": { "id": "...", "name": "Bob" }, "to": { "id": "...", "name": "Alice" }, "amount": 50.0 }
          ]
        }
        ```

---

## 💡 Notas para o Frontend
1.  **Imutabilidade:** O ID de um grupo ou transação nunca muda.
2.  **Membros Virtuais:** Membros de grupo podem não ter um `userId`. O frontend deve lidar com membros que são apenas strings.
3.  **Saldo:** A API permite saldos negativos em carteiras (útil para representar faturas de cartão de crédito).
4.  **Arredondamento:** A API trata arredondamentos na divisão de despesas para garantir que a soma das partes seja sempre igual ao total.

---

## 🎨 Sugestão de UI (User Experience)
*   **Dashboard:** Gráfico de receitas vs despesas e saldo total das carteiras.
*   **Módulo de Grupos:** Lista de grupos, indicador visual de "Você deve" ou "Te devem" em cada grupo.
*   **Criação de Despesa:** Interface dinâmica que muda os campos de entrada baseada no `splitType` selecionado.
