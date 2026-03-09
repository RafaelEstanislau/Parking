# Estapar Parking System

Backend de gerenciamento de estacionamento inteligente, integrado a um simulador externo via webhooks.

---

## Pré-requisitos

- Java 21+
- Maven 3.8+
- Docker e Docker Compose

---

## Rodar Localmente

### 1. Subir as dependências com Docker Compose

```bash
docker compose up -d
```

Isso sobe:
- **MySQL 8.0** na porta `3306`
- **Simulador** (`garage-sim`) na porta `3000`, disponível em `http://localhost:3000`

### 2. Rodar a aplicação

```bash
mvn spring-boot:run
```

A aplicação iniciará na porta `3003` e automaticamente:
- Consultará `GET http://localhost:3000/garage` no simulador
- Persistirá os setores e vagas no banco

### 3. Parar as dependências

```bash
docker compose down
```

Para remover também o volume do banco de dados:

```bash
docker compose down -v
```

---

## Rodar os Testes

Não requer nenhuma dependência externa — usa banco H2 em memória.

```bash
mvn test
```

---

## Configuração

O arquivo de configuração está em `src/main/resources/application.yml`.

| Propriedade | Valor padrão | Descrição |
|---|---|---|
| `server.port` | `3003` | Porta da aplicação |
| `spring.datasource.url` | `jdbc:mysql://localhost:3306/parking_db` | URL do banco |
| `spring.datasource.username` | `root` | Usuário do banco |
| `spring.datasource.password` | `root` | Senha do banco |
| `simulator.base-url` | `http://localhost:3000` | URL base do simulador |

---

## Endpoints

### POST /webhook

Recebe eventos do simulador.

**ENTRY** — entrada de veículo:
```bash
curl -X POST http://localhost:3003/webhook \
  -H "Content-Type: application/json" \
  -d '{
    "license_plate": "ZUL0001",
    "entry_time": "2025-01-01T12:00:00.000Z",
    "event_type": "ENTRY"
  }'
```

**PARKED** — veículo estacionado em uma vaga:
```bash
curl -X POST http://localhost:3003/webhook \
  -H "Content-Type: application/json" \
  -d '{
    "license_plate": "ZUL0001",
    "lat": -23.561684,
    "lng": -46.655981,
    "event_type": "PARKED"
  }'
```

**EXIT** — saída de veículo:
```bash
curl -X POST http://localhost:3003/webhook \
  -H "Content-Type: application/json" \
  -d '{
    "license_plate": "ZUL0001",
    "exit_time": "2025-01-01T13:10:00.000Z",
    "event_type": "EXIT"
  }'
```

---

### GET /revenue

Consulta a receita de um setor em uma data específica.

```bash
curl -X GET http://localhost:3003/revenue \
  -H "Content-Type: application/json" \
  -d '{
    "date": "2025-01-01",
    "sector": "A"
  }'
```

**Resposta:**
```json
{
  "amount": 20.00,
  "currency": "BRL",
  "timestamp": "2025-01-01T13:10:00.000Z"
}
```

---

## Estrutura do Projeto

```
src/main/java/com/estapar/parking/
├── domain/                  # Entidades, value objects, serviços e interfaces de repositório
│   ├── entity/
│   ├── valueobject/
│   ├── service/
│   ├── repository/
│   └── exception/
├── application/             # Casos de uso e DTOs
│   ├── usecase/
│   ├── dto/
│   └── port/
├── infrastructure/          # JPA, cliente HTTP, configurações Spring
│   ├── persistence/
│   │   ├── entity/
│   │   ├── repository/
│   │   └── adapter/
│   ├── client/
│   └── configuration/
└── interfaces/              # Controllers REST e webhook
    ├── rest/
    └── webhook/
```

