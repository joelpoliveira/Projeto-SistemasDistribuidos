# Backend

## Setup

### Base de dados

Existem duas maneiras de correr o containner com a base de dados:

- Abrir o vscode na pasta backend e selecionar Open remote window > Reopen in containner
- Usar o comando `docker-compose up` para correr separadamente

É possível aceder CLI do postegresql com o comando: `psql postgres://postgres:admin@localhost:6000/projeto-sd`

### API

- Usar o comando `./mvnw spring-boot:run` ou `./run`

## Endpoints

### Random

- [/home](http://127.0.0.1:8080/home)

### Auth

- [/auth/register](http://127.0.0.1:8080/auth/register)
- [/auth/login](http://127.0.0.1:8080/auth/login)
- [/auth/logout](http://127.0.0.1:8080/auth/logout)

### Team

- [/team/create](http://127.0.0.1:8080/team/create)
