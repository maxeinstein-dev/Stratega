# 🏗️ Arquitetura e Detalhes Técnicos (Stratega Backend)

Bem-vindo à documentação técnica do Stratega. Este documento detalha a fundação arquitetural, os diferenciais técnicos e a stack utilizada na construção desta API Financeira.

---

## 🏛️ Arquitetura Hexagonal (Ports & Adapters)

O projeto adota a **Arquitetura Hexagonal** para garantir que as regras de negócio sejam independentes de frameworks, bancos de dados e interfaces externas.

### Estrutura de Pastas
```text
stratega
 ┣ src/main/java/br/com/maxsueleinstein/stratega
 ┃ ┣ domain/         # Núcleo: Entidades ricas, Regras de Negócio e Interfaces de Repositório (Ports)
 ┃ ┣ application/    # Casos de Uso (Interactors), Portas de Entrada e DTOs
 ┃ ┣ infrastructure/ # Adaptadores Secundários: Persistência (JPA), Segurança (JWT), Mappers Manuais
 ┃ ┗ presentation/   # Adaptadores Primários: Controladores REST (Spring MVC)
```

---

## 💎 Diferenciais Técnicos

*   **Mappers Manuais:** Controle total na conversão entre entidades JPA e objetos de domínio, sem depender de bibliotecas automáticas (como MapStruct), garantindo clareza no mapeamento e evitando efeitos colaterais silenciosos.
*   **Domain-Driven Design (DDD):** Modelagem rica onde o domínio protege suas próprias invariantes (ex: não é possível adicionar fundos negativos a uma meta, não é possível criar transações no futuro além do permitido).
*   **TDD (Test-Driven Development):** Cobertura extensiva de testes unitários e de integração, garantindo a precisão matemática dos cálculos financeiros e dos relatórios de Dashboard.
*   **Segurança em Camadas:** O `JwtAuthenticationFilter` injeta a identidade validada diretamente no contexto do Spring Security, garantindo que usuários nunca consigam interagir com IDs de terceiros (Isolamento Multitenant).

---

## 🛠️ Tecnologias Utilizadas

A stack foi escolhida para alinhar alta performance, segurança robusta e um ecosistema de desenvolvimento moderno:

| Tecnologia | Função no Projeto |
| :--- | :--- |
| **Java 25** | Versão mais recente da linguagem, trazendo máxima performance, *pattern matching* e robustez. |
| **Spring Boot 4** | Framework base, provendo injeção de dependências e configuração auto-mágica. |
| **Spring Security + JWT** | Proteção blindada de endpoints e sessões *stateless* eficientes. |
| **Spring Data JPA** | Abstração de persistência sobre o Hibernate, orquestrando entidades. |
| **PostgreSQL** | Banco de Dados Relacional principal para produção. |
| **H2 Database** | Banco de dados em memória, otimizado para testes e desenvolvimento rápido. |
| **JUnit 5 & Mockito** | Conjunto completo e poderoso para assegurar a qualidade do código. |
| **Lombok** | Remoção agressiva de *boilerplate* (getters, setters, construtores) em classes de infra. |

---

> Para visualizar o planejamento de entregas e os módulos concluídos, acesse o [Roadmap de Desenvolvimento](roadmap.md).
