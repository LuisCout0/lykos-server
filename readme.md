```markdown
src/
 └── main/
     ├── java/
     │    └── com/
     │         └── lykos/
     │              ├── api/                     # Controllers e DTOs
     │              │    ├── controller/         # Endpoints REST
     │              │    └── dto/                # Objetos de transferência de dados
     │              │
     │              ├── application/             # Casos de uso / Serviços de aplicação
     │              │    └── service/
     │              │
     │              ├── domain/                  # Camada de domínio puro
     │              │    ├── model/              # Entidades JPA + Enums
     │              │    ├── repository/         # Interfaces JPA/Hibernate
     │              │    └── exception/          # Exceções customizadas do domínio
     │              │
     │              ├── infrastructure/          # Integrações externas
     │              │    ├── config/             # Configurações do Spring, Beans, CORS
     │              │    ├── security/           # JWT, autenticação, autorização
     │              │    ├── payment/            # Integrações com APIs de pagamento
     │              │    └── scheduler/          # Tarefas agendadas (ex.: limpeza de agendamentos)
     │              │
     │              └── LykosApplication.java    # Classe principal Spring Boot
     │
     └── resources/
          ├── application.properties             # Configuração do Spring Boot
          ├── messages.properties                # Mensagens para i18n
          ├── schema.sql                         # Scripts de criação inicial do DB (opcional)
          ├── data.sql                           # Dados iniciais para teste
          └── static/                            # Arquivos estáticos (se precisar)
```