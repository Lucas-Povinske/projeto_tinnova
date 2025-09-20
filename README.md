# Projeto Tinnova (Java + PostgreSQL + Angular SPA)

Projeto completo do desafio da Tinnova com:
- **Back-end**: Java 17, Spring Boot (API REST em `/veiculos`), JPA/Hibernate, PostgreSQL  
- **Front-end**: Angular (standalone SPA — uma única página com formulário + lista + estatísticas no cliente)  
- **Banco**: PostgreSQL

A SPA (Single Page Application) consome a API via o caminho relativo /veiculos.
Os arquivos estão containerizados via Docker e o Nginx faz o proxy para a API.

---
## 📑 Como executar os desafios 1 a 4

Os desafios 1 a 4 (Bubble Sort, Fatorial, Múltiplos e Votação) estão na pasta `back-end/src/main/java/com/tinnova/` como classes Java independentes.
Cada um deles tem um método `main` para execução direta via IDE ou linha de comando (Maven).

---

## 🚀 Como executar o desafio 5 (Docker Compose)

### Pré-requisitos
- Docker Desktop (ou Docker Engine + Compose v2)
- Portas livres: **5432** (DB), **8080** (API), **4200** (WEB)

> **Importante (front):** no service do Angular, deixe a base **relativa**  
> `private readonly base = '/veiculos';`  
> Assim o proxy (dev) / Nginx (prod) encaminha corretamente para a API.

### Subir
Na **raiz do repositório** (onde está `docker-compose.yml`):

```bash
docker compose up --build
```

Acesse:
- **Web (SPA)** → http://localhost:4200
- **API** → http://localhost:8080/veiculos

Parar e limpar (inclui volume do Postgres):

```bash
docker compose down -v
```

### O que sobe (compose):
- **db:** Postgres 16 (user: `tinnova` / pass: `tinnova` / db: `tinnova`)
- **api:** Spring Boot apontando para `db:5432` (mesma rede do compose)
- **web:** Angular construído e servido por Nginx (proxy `/veiculos` → `api:8080`)

---
## 📡 Endpoints da API

| Método | Rota             | Descrição                                    |
| -----: | ---------------- | -------------------------------------------- |
|    GET | `/veiculos`      | Lista veículos (filtros `?marca=&ano=&cor=`) |
|    GET | `/veiculos/{id}` | Detalhe                                      |
|   POST | `/veiculos`      | Cria                                         |
|    PUT | `/veiculos/{id}` | Atualiza (completo)                          |
|  PATCH | `/veiculos/{id}` | Atualiza parcial                             |
| DELETE | `/veiculos/{id}` | Remove                                       |


### Exemplo de criação (`POST /veiculos`):
```json
{
  "veiculo": "Civic",
  "marca": "Honda",
  "ano": 2020,
  "cor": "Preto",
  "descricao": "Sedan",
  "vendido": false
}
```

---
## 💻 Rodar localmente (sem Docker)

### Banco
- DB: `tinnova` — User: `tinnova` — Pass: `tinnova` — Porta: `5432`

### Back-end (Java 17 + Maven)
1) Em `src/main/resources/application.properties` (dev):
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/tinnova
spring.datasource.username=tinnova
spring.datasource.password=tinnova
spring.jpa.hibernate.ddl-auto=update
```

2) Rodar:
```bash
mvn clean spring-boot:run
# API em http://localhost:8080/veiculos
```

### Front-end (Node ≥ 18.19)

Instalar e subir:

```bash
cd front-end
npm ci
npm start -- --proxy-config proxy.conf.json
# SPA em http://localhost:4200
```
---
## 🗂️ Estrutura sugerida

```lua
projeto_tinnova/
├─ back-end/                  # back-end (Spring Boot)
│  ├─ src/
│  │  ├─ main/
│  │  │  ├─ java/com/tinnova/
│  │  │  │  ├─ bubble/BubbleSort.java
│  │  │  │  ├─ fatorial/Fatorial.java
│  │  │  │  ├─ multiplos/Multiplos.java
│  │  │  │  ├─ votacao/Votacao.java
│  │  │  │  └─ veiculo/
│  │  │  │     ├─ controller/VeiculoController.java
│  │  │  │     ├─ dto/VeiculoDto.java
│  │  │  │     ├─ model/Veiculo.java
│  │  │  │     ├─ repository/VeiculoRepository.java
│  │  │  │     └─ service/VeiculoService.java
│  │  │  │  
│  │  │  └─ resources/
│  │  │     └─ application.properties
│  │  └─ test/
│  └─ pom.xml
├─ front-end/                 # front-end (Angular)
│  ├─ package.json
│  ├─ angular.json
│  ├─ Dockerfile.web
│  └─ src/
│     ├─ app/
│     │  ├─ components/
│     │  ├─ services/
│     │  └─ app.component.ts
│     └─ index.html
├─ docker-compose.yml
├─ Dockerfile.api
└─ nginx.conf
```

---
## 🧰 Dicas & Troubleshooting

- Front envia POST em `http://localhost:4200/veiculos` e dá 404:
  - Use o proxy do Angular em dev ou rode via Docker/Nginx. 
  - Garanta que o service usa base RELATIVA (/veiculos).

- Postgres: "Credenciais não aplicadas"
  - As variáveis POSTGRES_* só valem na primeira inicialização do volume.
      Para mudar credenciais, rode: docker compose down -v e suba novamente.

- Conexão recusada - “cannot invoke … sqlExceptionHelper() is null” 
  - Geralmente DB indisponível: confirme URL (localhost:5432 ou db:5432 no Docker)
      e credenciais tinnova/tinnova.

---
## 📜 Licença
Todas as estruturas, desenvolvimento e projetos são para uso educacional e avaliativo.

