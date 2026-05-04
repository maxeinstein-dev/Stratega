# Stratega - API de Gestão Financeira Inteligente

> API robusta para planejamento e controle financeiro pessoal e em grupo.

Stratega é uma API REST moderna construída com **Java 25**, **Spring Boot 4** e fundamentada em **Arquitetura Hexagonal**. O projeto foca em alta testabilidade, isolamento de regras de negócio e escalabilidade, servindo tanto como uma ferramenta funcional quanto como um portfólio de engenharia de software de alta qualidade.

---

## 🌟 Visão Geral

O Stratega vai além do simples registro de gastos, oferecendo:

*   **Gestão de Usuários e Segurança:** Autenticação via JWT e criptografia de senhas.
*   **Carteiras (Wallets):** Múltiplas carteiras com suporte a saldo positivo e negativo (cartões de crédito).
*   **Transações Inteligentes:** Registro de receitas, despesas e transferências entre carteiras com atualização automática de saldo.
*   **Categorias Customizadas:** Organização flexível por usuário ou categorias globais.
*   **Módulo de Despesas em Grupo (Social Finance):** 
    *   Divisão de despesas entre múltiplos participantes.
    *   **4 Estratégias de Divisão:** Uniforme, Exata, Porcentagem e Por Cotas (Shares).
    *   **Algoritmo de Otimização:** Sugestão do número mínimo de transferências para liquidar dívidas do grupo.

---

## 🏗️ Arquitetura e Design

O projeto adota a **Arquitetura Hexagonal (Ports & Adapters)** para garantir que as regras de negócio sejam independentes de frameworks e tecnologias externas.

### Estrutura de Pastas
```text
stratega
 ┣ src/main/java/br/com/maxsueleinstein/stratega
 ┃ ┣ domain/         # Núcleo: Entidades, Regras de Negócio e Interfaces de Repositório
 ┃ ┣ application/    # Casos de Uso, Portas (Interfaces) e DTOs
 ┃ ┣ infrastructure/ # Adaptadores: Persistência (JPA), Segurança (JWT), Mappers Manuais
 ┃ ┗ presentation/   # Controladores REST
```

### Diferenciais Técnicos
*   **Mappers Manuais:** Controle total na conversão entre entidades JPA e objetos de domínio, sem dependência de bibliotecas como MapStruct.
*   **Domain-Driven Design (DDD):** Modelagem rica onde o domínio protege suas próprias invariantes.
*   **TDD (Test-Driven Development):** Cobertura extensiva de testes garantindo a precisão matemática dos cálculos financeiros.

---

## 🛠️ Tecnologias

*   **Java 25:** Versão mais recente com foco em performance e novos recursos da linguagem.
*   **Spring Boot 4:** Framework base para a aplicação.
*   **Spring Security + JWT:** Proteção de endpoints e gerenciamento de sessões stateless.
*   **Spring Data JPA / Hibernate:** Abstração de banco de dados com mapeamento objeto-relacional.
*   **H2 Database:** Banco de dados em memória para desenvolvimento e testes rápidos.
*   **JUnit 5 & Mockito:** Conjunto completo para testes de unidade e integração.
*   **Lombok:** Redução de boilerplate em classes de infraestrutura.

---

## 🚀 Como Executar

### Pré-requisitos
*   **Java 25** instalado.
*   **Maven 3.9+** instalado.

### Passo a Passo
1.  **Clonar o repositório:**
    ```bash
    git clone https://github.com/maxeinstein-dev/Stratega.git
    cd Stratega
    ```
2.  **Executar os testes (Recomendado):**
    ```bash
    ./mvnw test
    ```
3.  **Iniciar a aplicação:**
    ```bash
    ./mvnw spring-boot:run
    ```
4.  **Acessar a Documentação:**
    O Swagger/OpenAPI estará disponível em: `http://localhost:8080/swagger-ui.html`

---

## 📈 Roadmap Concluído

- [x] Arquitetura Hexagonal estabelecida.
- [x] Sistema de Autenticação JWT.
- [x] Gestão de Wallets com atualização de saldo em tempo real.
- [x] Transferências entre carteiras com transações vinculadas.
- [x] Módulo de Despesas em Grupo com 4 tipos de split.
- [x] Algoritmo de liquidação de dívidas (Suggested Transfers).
- [x] Cobertura de testes superior a 90% nas regras críticas.

---

## 👨‍💻 Autor

**Maxsuel Einstein** - Engenheiro de Software focado em soluções backend robustas.

[LinkedIn](https://www.linkedin.com/in/maxsueleinstein/) | [GitHub](https://github.com/maxeinstein-dev)

---

## 📄 Licença
Este projeto é para fins de estudo e demonstração técnica.
