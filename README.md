# Stratega

> API para planejamento e controle financeiro pessoal.

Stratega é uma API REST construída com **Java + Spring Boot** para gerenciamento financeiro. O objetivo do projeto é
permitir que usuários registrem transações, organizem carteiras financeiras, criem categorias personalizadas e planejem
suas finanças com mais clareza.

Este projeto também funciona como **projeto de portfólio**, demonstrando boas práticas de arquitetura backend, modelagem
de domínio e desenvolvimento de APIs.

---

# Visão Geral

O Stratega permite:

- Gerenciar **usuários**
- Criar **carteiras financeiras (wallets)**
- Registrar **transações** (receitas, despesas e transferências)
- Criar **categorias personalizadas**
- Dividir **despesas entre participantes**
- Organizar finanças por carteira e categoria

O sistema foi pensado para evoluir gradualmente até se tornar um **planejador financeiro completo**.

---

# Arquitetura

O projeto segue uma arquitetura em camadas comum em aplicações Spring:

Controller → Service → Repository → Database

### Camadas

**Controller**

Responsável por expor os endpoints da API.

**Service**

Contém as regras de negócio do sistema.

**Repository**

Responsável pela comunicação com o banco de dados através do Spring Data JPA.

**Domain (Entities)**

Modelagem das entidades do sistema.

---

# Tecnologias

- Java 21
- Spring Boot
- Spring Data JPA
- Hibernate
- Maven
- Swagger / OpenAPI
- H2 / PostgreSQL (planejado)

---

# Modelo de Domínio

Principais entidades do sistema:

### User

Representa um usuário do sistema.

### Wallet

Carteira financeira onde as transações são agrupadas.

Exemplos:

- Carteira Casa
- Carteira Lazer
- Carteira Investimentos

Wallets podem possuir **saldo negativo**, permitindo representar cartões de crédito.

### Transaction

Representa qualquer movimentação financeira.

Tipos:

- INCOME
- EXPENSE
- TRANSFER

Toda transação deve pertencer a uma **wallet**.

### Category

Categorias criadas pelo próprio usuário para organizar gastos.

Exemplo:

- Alimentação
- Casa
- Transporte

Categorias são **customizáveis por usuário**.

### Expense

Representa uma despesa compartilhada.

Nem toda transação é uma expense, mas **toda expense gera uma transaction**.

### ExpenseParticipant

Participantes envolvidos na divisão da despesa.

### ExpenseShare

Define quanto cada participante pagou ou deve pagar.

---

# Documentação da API

A documentação interativa da API é gerada automaticamente com **Swagger/OpenAPI**.

Após iniciar a aplicação:

```
http://localhost:8080/swagger-ui/index.html
```

Através da interface é possível:

- visualizar endpoints
- testar requisições
- visualizar modelos de request

---

# Como executar o projeto

## Pré-requisitos

- Java 21
- Maven
- Git

## Clonar repositório

```
git clone https://github.com/maxeinstein-dev/Stratega.git
```

## Executar aplicação

```
mvn spring-boot:run
```

A API iniciará em:

```
http://localhost:8080
```

---

# Roadmap

Próximas evoluções planejadas:

- Implementação completa dos serviços de domínio
- Atualização automática de saldo das wallets
- Sistema de transferências entre carteiras
- DTOs para requests e responses
- Autenticação com JWT
- Controle de usuários autenticados
- Relatórios financeiros
- Planejamento financeiro

O roadmap completo está disponível em:

```
docs/roadmap.md
```

---

# Estrutura do Projeto

```
stratega
 ┣ src
 ┃ ┣ controller
 ┃ ┣ service
 ┃ ┣ repository
 ┃ ┣ domain
 ┃ ┃ ┗ entity
 ┣ docs
 ┃ ┗ roadmap.md
 ┣ README.md
```

---

# Objetivo do Projeto

Este projeto tem dois objetivos principais:

1. Construir uma **API financeira funcional**.
2. Servir como **projeto de estudo e portfólio backend**.

O desenvolvimento é feito de forma incremental, começando por um **MVP funcional** e evoluindo gradualmente em
arquitetura e funcionalidades.

---

# Autor

Maxsuel Einstein Lair Leite Batista de Medeiros

GitHub

https://github.com/maxeinstein-dev

---

# Licença

Este projeto é destinado para fins educacionais e de portfólio.
