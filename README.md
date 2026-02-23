✅ Solução do bug EJB: transferência valida IDs/valor/saldo, aplica optimistic locking (`LockModeType.OPTIMISTIC_FORCE_INCREMENT`) e força rollback via `EJBException` em qualquer inconsistência.
# 🧩 BIP Teste — Solução Fullstack (Backend + Frontend)

Este projeto entrega uma aplicação completa para **gestão de benefícios**, com:

- **API REST no backend (Spring Boot)**
- **Interface web no frontend (HTML/CSS/JS puro)**
- **Banco H2 em memória com carga automática de schema e dados iniciais**

---

## 📌 Visão geral da arquitetura

A solução está organizada em camadas para separar responsabilidades:

- **Controller (API):** recebe requisições HTTP, valida entrada e delega para service.
- **Service (regra de negócio):** concentra regras de CRUD e transferência de saldo.
- **Repository (acesso a dados):** persistência com Spring Data JPA.
- **Model/Entity:** representação da tabela `BENEFICIO` no banco.
- **DTO + Mapper:** contratos de entrada/saída e mapeamento entre DTO e Entity via MapStruct.
- **Frontend SPA leve:** tela única com operações de CRUD + transferência consumindo API.

---

## ⚙️ Backend — o que foi utilizado

### Stack e dependências principais

- **Java 17**
- **Spring Boot 3.2.5**
- **Spring Web** (API REST)
- **Spring Validation** (validação de payload)
- **Spring Data JPA** (persistência)
- **H2 Database** (banco em memória)
- **MapStruct** (mapeamento DTO ↔ Entity)
- **Lombok** (boilerplate)
- **Spring Boot Test + MockMvc** (testes de integração)

### Estrutura técnica (backend-module)

- `model/Beneficio`: entidade JPA com controle de versão (`@Version`) para locking otimista.
- `repository/BeneficioRepository`: CRUD via `JpaRepository`.
- `dto/BeneficioRequest`, `dto/BeneficioResponse`, `dto/TransferRequest`: contratos da API.
- `mapper/BeneficioMapper`: conversão automática entre DTO e entidade usando MapStruct.
- `service/BeneficioService`: regras de negócio e transações (`@Transactional`).
- `controller/BeneficioController`: endpoints REST.
- `controller/ApiExceptionHandler`: padronização de erros (404, 400, 409 e validação).

### Regras de negócio implementadas

#### CRUD de benefícios

- **Listar** todos os benefícios.
- **Buscar por ID** com retorno 404 quando não encontrado.
- **Criar** benefício com validações de entrada.
- **Atualizar** benefício existente.
- **Excluir** benefício por ID.

#### Transferência de saldo entre benefícios

A transferência segue validações importantes:

1. Origem e destino **não podem ser o mesmo ID**.
2. Ambos os benefícios precisam estar **ativos**.
3. O benefício de origem precisa ter **saldo suficiente**.
4. A operação é executada em **transação** para manter consistência.
5. A entidade possui campo `version` com `@Version` para **concorrência otimista**.

### Validações de entrada (Bean Validation)

- `BeneficioRequest`
  - `nome`: obrigatório (`@NotBlank`)
  - `valor`: obrigatório e mínimo `0.00` (`@NotNull`, `@DecimalMin`)
  - `ativo`: obrigatório (`@NotNull`)
- `TransferRequest`
  - `fromId` e `toId`: obrigatórios
  - `amount`: obrigatório e mínimo `0.01`

### Banco de dados

- Tabela `BENEFICIO` com colunas: `ID`, `NOME`, `DESCRICAO`, `VALOR`, `ATIVO`, `VERSION`.
- Scripts de inicialização:
  - `schema.sql` (estrutura)
  - `data.sql` (dados seed)
- Configuração no `application.properties` para subir com H2 em memória na porta da API.

### Endpoints principais

Base URL: `http://localhost:9090/api/v1/beneficios`

- `GET /` → lista benefícios
- `GET /{id}` → busca benefício por ID
- `POST /` → cria benefício
- `PUT /{id}` → atualiza benefício
- `DELETE /{id}` → remove benefício
- `POST /transfer` → transfere saldo entre benefícios

### Como executar o backend

```bash
cd backend-module
mvn spring-boot:run
```

---

## 🖥️ Frontend — o que foi feito

### Stack utilizada

- **HTML5 + CSS3 + JavaScript (Vanilla JS)**
- **Node.js (http server simples)** para servir arquivos estáticos
- **Sem frameworks/dependências externas** no frontend

### Funcionalidades implementadas

- **Listagem** de benefícios em tabela.
- **Contador de itens** carregados.
- **Cadastro** de novo benefício.
- **Edição** de benefício existente.
- **Exclusão** com confirmação.
- **Transferência de saldo** entre benefícios ativos.
- **Feedback visual** de sucesso/erro para cada ação.
- **Atualização manual** da lista (botão “Atualizar lista”).
- **Layout responsivo** para desktop/mobile.

### Lógica do frontend

- `app.js` centraliza:
  - chamadas HTTP (`fetch`) para a API do backend,
  - tratamento padronizado de erro por resposta JSON,
  - renderização da tabela,
  - gestão de estado local (`beneficios`),
  - fluxo de formulário (novo/edição),
  - fluxo de transferência e atualização da UI.
- `styles.css` define tema visual, componentes (cards, botões, tabela, badges) e responsividade.
- `server.js` implementa servidor estático simples com content-type por extensão e proteção básica de path traversal.

### Como executar o frontend

```bash
cd frontend
npm run start
```

Aplicação disponível em: `http://localhost:4200`

---

## ✅ Testes

No backend existe teste de integração com `MockMvc` cobrindo:

- listagem de dados seed,
- transferência de valor entre benefícios.

Para executar:

```bash
cd backend-module
mvn test
```

---

## 📂 Estrutura do repositório

- `backend-module/` → API Spring Boot
- `frontend/` → interface web
- `db/` → scripts SQL base (`schema.sql`, `seed.sql`)
- `docs/` → contexto do desafio

---

## 🚀 Resultado

Projeto fullstack funcional com documentação consolidada, camada de negócio no backend (incluindo transferência com validações e transação), mapeamento com MapStruct, banco H2 para execução local rápida e frontend completo para operação ponta a ponta.
