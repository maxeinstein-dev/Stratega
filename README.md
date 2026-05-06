<div align="center">
  
# 🚀 Stratega - API Financeira Inteligente
  
[![Java](https://img.shields.io/badge/Java-25-orange?style=for-the-badge&logo=java)](#)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.x-brightgreen?style=for-the-badge&logo=spring)](#)
[![JWT](https://img.shields.io/badge/Secured_with-JWT-blue?style=for-the-badge)](#)
[![Hexagonal](https://img.shields.io/badge/Architecture-Hexagonal-blueviolet?style=for-the-badge)](#)
[![License](https://img.shields.io/badge/License-MIT-gray?style=for-the-badge)](#)

*Assuma o controle total do seu patrimônio com uma API moderna, testável e pronta para produção.*

---
</div>

## 🌟 O que é o Stratega?

O **Stratega** é muito mais do que um simples "anotador de gastos". Ele é o verdadeiro motor de um ecossistema de planejamento financeiro, suportando rotinas avançadas que ajudam você e seus amigos a manterem as finanças no azul.

Ele foi construído focado no mais alto nível de engenharia de software (Clean Architecture / Hexagonal), permitindo máxima testabilidade e performance matemática.

## ✨ Destaques & Funcionalidades

💳 **Carteiras Flexíveis**  
Suporte para múltiplas carteiras simultâneas, moedas globais (USD, EUR, BTC) e suporte nativo a saldo negativo para você gerenciar seus cartões de crédito.

📊 **Dashboards Analíticos**  
Rotas especializadas para retornar agrupamentos de faturamento, despesas por categoria e evolução histórica (filtros de 30 a 365 dias) desenhados para otimizar renderizações em gráficos.

🎯 **Metas e Objetivos (Savings Goals)**  
Módulo dedicado para planejar o futuro (ex: *Viagem, Carro*), recebendo injeções de fundos e calculando porcentagens de progresso automaticamente.

🤝 **Social Finance (Despesas em Grupo)**  
Divida o happy hour ou a viagem sem confusão. O Stratega suporta divisões Exatas, Uniformes, Porcentagem ou Cotas, e gera transferências sugeridas que liquidam dívidas de forma otimizada injetando saldo direto na sua carteira.

🔁 **Recorrência e Automação**  
Criação automática de compras parceladas ou despesas fixas recorrentes projetando seus impactos futuros.

---

## 📚 Documentação Aprofundada

Gosta de entender o funcionamento do motor por baixo do capô? Nós preparamos documentos detalhados sobre as escolhas arquiteturais e a evolução da nossa API. 

👉 **[Arquitetura e Stack Tecnológica (ARCHITECTURE.md)](docs/ARCHITECTURE.md)**  
Entenda como aplicamos *Hexagonal Architecture*, por que utilizamos Java 25 e detalhes sobre nossos Mappers manuais.

👉 **[Roadmap e Módulos Concluídos (ROADMAP.md)](docs/roadmap.md)**  
Acompanhe toda a nossa jornada de desenvolvimento, desde a criação do MVP até os mais avançados gráficos.

---

## ⚙️ Como Executar Rapidamente

Quer testar localmente? É simples. Tenha o **Java 25** e o **Maven** instalados:

```bash
# 1. Clone o repositório
git clone https://github.com/maxeinstein-dev/Stratega.git
cd Stratega/Stratega-Back

# 2. Rode os testes unitários de garantia (opcional)
./mvnw test

# 3. Inicie o Servidor Backend (Embutido)
./mvnw spring-boot:run
```

Acesse o **Swagger** para testar as rotas em: `http://localhost:8081/swagger-ui/index.html`

---

## 👨‍💻 Autor

Feito com dedicação arquitetural por **Maxsuel Einstein** - Engenheiro de Software.  
Conecte-se comigo: [LinkedIn](https://www.linkedin.com/in/maxsueleinstein/) | [GitHub](https://github.com/maxeinstein-dev)
