## Sistema de planjemaneto e controle de produção

Esse projeto é a resposta para um desafio técnico envolvendo o desenvolvimento de um sistema de planejamento e controle de produção que implementa as seguintes funcionalidades:

- Cadastro de matérias-primas, com infomrações como nome e quantidade disponível em estoque
- Cadastro de produtos, com informações como preço, matérias-primas necessárias para sua produção e quantidade
- Recomendação de produção otimizada para gerar o máximo de lucro possível considerando o estoque de matéria-prima disponível e o preço dos produtos

## Tecnologias utilizadas
### Backend
- Java
- Quarkus
- Postegres

### Frontend
- React
- Next.js
- Componentes Shadcn
- Tanstack query

## Como rodar o backend

Clone o repositório:

```bash
git clone git@github.com:william-gtatim/pcp-system-api.git
cd pcp-system-api
```

Na pasta do projeto, execute o comando abaixo para subir o banco de dados PostgreSQL:

```bash
docker compose up -d
```

Em seguida, rode a aplicação com o Quarkus:

```bash
quarkus dev
```

## Como rodar o frontend

Clone o repositório do frontend:

```bash
git clone git@github.com:william-gtatim/pcp-system-frontend.git
cd pcp-system-frontend
```

Instale as dependências:

```bash
npm install
```

Rode a aplicação:

```bash
npm run dev
```

### Importante

- Certifique-se de que o backend esteja rodando em `http://localhost:8080`, ou ajuste a URL da api em `libs/apiClient`.
- Garanta que o frontend esteja rodando em `http://localhost:3000` para evitar problemas de CORS.


