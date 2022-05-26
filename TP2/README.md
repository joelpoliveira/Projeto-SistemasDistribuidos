# Projeto 2 SD

## Setup

### Base de dados

- Usar o comando `docker-compose up` para correr o containner. 
- É possível usar a flag `-d` para o containner ficar a correr em background, não sendo preciso vários terminais.
- Usar o comando `docker-compose down` ou `docker stop projeto-sd` para parar o containner.

É possível aceder CLI do postegresql com o comando: `psql postgres://postgres:admin@localhost:6000/projeto-sd`

### API

- Usar o comando `java -jar sd.war`

### ENV

Adicionar uma chave de autenticação da [Sport-API](https://api-sports.io/) para poder usar dados desta API.

