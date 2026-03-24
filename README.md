# Bruno Dev API - Backend Portfolio

Esta é a API RESTful desenvolvida para gerenciar o backend de um portfólio profissional, sistema de feedbacks e autenticação de usuários. O projeto foi arquitetado com um forte foco em engenharia de software moderna, priorizando a manutenibilidade, testabilidade e o baixo acoplamento.

## Arquitetura e Padrões de Projeto

O sistema foi desenhado utilizando conceitos de **Clean Architecture** (Arquitetura Limpa) e **Domain-Driven Design (DDD)**, afastando-se do padrão MVC tradicional para adotar uma separação rigorosa de responsabilidades (SRP - *Single Responsibility Principle*). 

A base de código está dividida nas seguintes camadas:

* **Domain:** O núcleo do sistema. Contém as entidades de negócio (ex: `User`, `Project`, `Feedback`) e exceções de domínio. É uma camada isolada, sem dependências de frameworks externos (como Spring ou JPA).
* **Application (Use Cases):** Orquestra as regras de negócio. Em vez de utilizar classes de serviço genéricas ("God Classes"), o sistema emprega Casos de Uso com foco único (ex: `RegisterUserUseCase`, `ProcessGoogleLoginUseCase`, `ActivateAccountUseCase`).
* **Infrastructure:** Gerencia a comunicação com o mundo externo. Contém implementações de repositórios (Spring Data JPA), configurações de segurança (Spring Security/JWT), serviços de envio de e-mail integrados à API externa (Brevo via WebClient) e tratamento global de exceções.
* **API / Controllers:** A porta de entrada da aplicação. Responsável unicamente por receber requisições HTTP, validar os contratos de entrada (DTOs) e repassar o processamento para a camada de Application.

## Tecnologias Utilizadas

* **Linguagem:** Java 21
* **Framework:** Spring Boot 3.4.2
* **Segurança:** Spring Security + JWT (JSON Web Tokens)
* **Persistência:** Spring Data JPA / Hibernate
* **Banco de Dados:** H2 Database (Ambiente de desenvolvimento)
* **Provedor de E-mail:** Brevo API

## Funcionalidades Principais

### Módulo de Usuários (Auth & User)
* Cadastro de usuários com validação via código de 6 dígitos enviado por e-mail.
* Autenticação via credenciais tradicionais (JWT).
* Integração com Login Social (Google OAuth2).
* Fluxo completo de recuperação de senha e alteração de dados de perfil.
* Envio de e-mails transacionais (Boas-vindas, alertas de segurança, lembrete de usuário).

### Módulo de Projetos (Portfolio)
* Gerenciamento de projetos exibidos no portfólio com CRUD completo, possibilitando o frontend criar projetos dinamicamente.
* Suporte a galerias de imagens, ficha técnica e passos de configuração (setup steps).

### Módulo de Feedback
* Recepção e armazenamento de avaliações (notas e comentários).
* Disparo de alertas automáticos para o administrador a cada novo feedback submetido.

## Estrutura de Diretórios (Resumo)

A organização interna reflete a arquitetura descrita acima:

```text
src/main/java/com/brunofragadev/
├── module/
│   ├── auth/         # Contratos e respostas de autenticação
│   ├── feedback/     # Regras e rotas do sistema de avaliação
│   ├── project/      # Gestão do portfólio e galerias
│   └── user/
│       ├── api/               # Controllers e DTOs (Request/Response)
│       ├── application/       # Use Cases (SRP)
│       ├── domain/            # Entidades ricas e Exceções de negócio
│       └── infrastructure/    # Mappers e Repositories
├── infrastructure/
│   ├── config/       # Configurações globais (Security, Audit, JWT)
│   ├── email/        # Integração com a API do Brevo
│   └── handler/      # Global Exception Handler
└── shared/           # Utilitários e formatadores comuns
