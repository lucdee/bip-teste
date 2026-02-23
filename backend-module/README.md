# Backend Module

API Spring Boot para gerenciamento de benefícios.

## Como rodar

```bash
cd backend-module
mvn spring-boot:run
```

## Endpoints principais

- `GET /api/v1/beneficios`
- `GET /api/v1/beneficios/{id}`
- `POST /api/v1/beneficios`
- `PUT /api/v1/beneficios/{id}`
- `DELETE /api/v1/beneficios/{id}`
- `POST /api/v1/beneficios/transfer`

Banco H2 em memória é inicializado automaticamente com `schema.sql` e `data.sql`.
