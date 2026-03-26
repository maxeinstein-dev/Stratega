# Stratega --- Backend MVP Roadmap

Este roadmap considera que as seguintes etapas **já foram concluídas**:

-   Base do projeto Spring Boot
-   Entidades criadas
-   Repositories criados

O foco agora é **implementar a lógica de negócio nos Services e expor
via Controllers até chegar no MVP funcional.**

------------------------------------------------------------------------

# Fase 4 --- Services (Lógica de Negócio)

Objetivo: Centralizar regras de negócio.

Estrutura padrão:

Controller → Service → Repository

Nunca acessar Repository diretamente no Controller.

## Services necessários

UserService\
WalletService\
CategoryService\
TransactionService

------------------------------------------------------------------------

# TransactionService

## 1. Criar transação

Método:

createTransaction()

Responsabilidades:

-   verificar se categoria existe
-   criar categoria se não existir
-   associar categoria à transação
-   salvar transação
-   atualizar saldo da carteira

Fluxo:

1 Receber Transaction\
2 Verificar Category\
3 Criar Category se necessário\
4 Salvar Transaction\
5 Atualizar saldo da Wallet

------------------------------------------------------------------------

## 2. Transferência entre carteiras

Método:

createTransfer()

Responsabilidades:

-   validar carteiras
-   debitar carteira origem
-   creditar carteira destino
-   registrar transaction do tipo TRANSFER

Fluxo:

1 Receber originWalletId\
2 Receber destinationWalletId\
3 Receber amount\
4 Atualizar saldo origem\
5 Atualizar saldo destino\
6 Salvar transaction

------------------------------------------------------------------------

# Fase 5 --- Controllers (API REST)

Objetivo: Expor endpoints para o sistema.

## TransactionController

Endpoints necessários:

POST /transactions\
GET /transactions\
POST /transactions/transfer

------------------------------------------------------------------------

## Criar transação

Endpoint:

POST /transactions

Exemplo de JSON:

{ "description": "Conta de energia", "amount": 200, "type": "EXPENSE",
"wallet": { "id": 1 }, "category": { "name": "Casa", "type": "EXPENSE" }
}

Fluxo interno:

Controller recebe request\
→ chama TransactionService\
→ retorna transaction salva

------------------------------------------------------------------------

## Listar transações

Endpoint:

GET /transactions

Melhorias futuras:

GET /transactions?wallet=1\
GET /transactions?category=2

------------------------------------------------------------------------

## Transferência

Endpoint:

POST /transactions/transfer

Exemplo:

{ "originWalletId": 1, "destinationWalletId": 2, "amount": 500 }

Fluxo:

Service debita origem\
Service credita destino\
Service registra transaction

------------------------------------------------------------------------

# Fase 6 --- Atualização de saldo da Wallet

Regra:

INCOME → soma saldo\
EXPENSE → subtrai saldo\
TRANSFER → move entre carteiras

Implementar dentro do TransactionService.

------------------------------------------------------------------------

# Fase 7 --- Validações básicas

Adicionar validações:

-   valor da transação maior que zero
-   carteira deve existir
-   não permitir transferência para mesma carteira

------------------------------------------------------------------------

# Fase 8 --- Testes manuais da API

Testar usando:

Postman ou Insomnia

Fluxos para testar:

Criar Wallet\
Criar Categoria\
Criar Transaction\
Listar Transactions\
Criar Transferência

------------------------------------------------------------------------

# MVP Concluído

O MVP estará completo quando for possível:

Criar usuário\
Criar carteiras\
Criar categorias\
Criar transações\
Transferir valores entre carteiras\
Consultar saldo das carteiras\
Listar transações

------------------------------------------------------------------------

# Próximas evoluções (Pós-MVP)

Expense Sharing

Expense\
ExpenseParticipant\
ExpenseShare

Planejamento financeiro

Orçamentos mensais

Dashboard financeiro

Autenticação

JWT + Spring Security

Frontend

Aplicação web ou mobile

------------------------------------------------------------------------

# Meta do projeto

Objetivo inicial:

Backend financeiro funcional capaz de:

-   registrar movimentações
-   organizar por carteiras
-   organizar por categorias
-   realizar transferências internas

Esse é o **núcleo do Stratega**.
