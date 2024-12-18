# API - CRUD - KOTLIN - KTOR

Bem-vindo à **API**, uma aplicação demonstrativa desenvolvida com **Kotlin** e conectada a um banco de dados PostgreSQL. O objetivo deste projeto é fornecer endpoints para gerenciamento de usuários, incluindo registro, autenticação e manipulação de dados básicos.

![Platform](https://img.shields.io/badge/platform-Kotlin-blue.svg)
![Database](https://img.shields.io/badge/database-PostgreSQL-blue.svg)  
![Framework](https://img.shields.io/badge/framework-Ktor-blue.svg)  

---

## **Índice**

- [Visão geral](#visão-geral)
- [Recursos](#recursos)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Instalação](#instalação)
  - [Pré-requisitos](#pré-requisitos)
  - [Configurando o banco de dados](#configurando-o-banco-de-dados)
  - [Executando a API](#executando-a-api)
  - [Endpoints Disponíveis](#endpoints-disponíveis)
  - [Exemplos com curl](#exemplos-com-curl)
- [Testando a API](#testing-the-api)
- [Contactos](#contactos)

---

## **Visão Geral**

Esta API é projetada para manipular operações básicas de gerenciamento de usuários, oferecendo endpoints RESTful simples e uma estrutura organizada em **Kotlin**.

A API pode ser integrada a sistemas maiores, sendo facilmente extensível para suportar mais recursos no futuro.

---

## **Recursos**

- **Registro de Usuários**: Criação de novos usuários com nome, e-mail e senha.
- **Edição, exclusão, consulta de Usuarios**: Consulta, exclusão, edição ed usuarios.
- **Estrutura Limpa**: Organizada com pastas `data`, `db`, `model` e `services`.
- **Fácil Configuração**: Pode ser adaptada rapidamente para diferentes bancos de dados.

---

## **Estrutura do Projeto**

A estrutura do projeto está organizada da seguinte forma:

```plaintext
src
└── main
    └── kotlin
        └── com.example
            ├── data    // DTOs (Data Transfer Objects)
            │   └── UserRequest.kt
            │
            ├── db  // Conexão com o Banco de Dados
            │   └── Connectiondb.kt
            │
            ├── model   // Modelos de Domínio
            │   └── User.kt
            │
            └── services    // Lógica de Serviços
                ├── IUserServices.kt
                └── UserServices.kt
```
## **Instalação**

### **Pré-requisitos**

Antes de iniciar o projeto, você precisará instalar os seguintes recursos:

- [IntelliJ IDEA](https://www.jetbrains.com/idea/)
- [PostgreSQL](https://www.postgresql.org/download/)
- [Gradle](https://gradle.org/install/)

---

### **Configurando o banco de dados**

1. **Instale o PostgreSQL** e crie um novo banco de dados chamado `teste_kotlin`.

2. **Configure a conexão com o banco de dados** no arquivo `Connectiondb.kt`:

   ```kotlin
   import kotlinx.coroutines.Dispatchers
   import org.jetbrains.exposed.sql.Database
   import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

   object Connectiondb {
       // Função para inicializar a conexão com o banco de dados e criar a tabela Users se não existir.
       fun init() {
           val database = Database.connect(
               url = "jdbc:postgresql://localhost:5432/teste_kotlin", // URL do banco de dados
               driver = "org.postgresql.Driver", // Driver JDBC para PostgreSQL
               user = "postgres", // Usuário do banco
               password = "Leonel.3" // Senha do banco
           )
           transaction(database) {
            SchemaUtils.create(Users) // Cria a tabela 'Users' se não existir
        }
       }

       // Função suspensa para executar uma transação no banco de dados de forma assíncrona.
       suspend fun <T> dbQuery(block: () -> T): T =
           newSuspendedTransaction(Dispatchers.IO) { block() }
   }

### Executando a API

Para rodar a API localmente, siga os passos abaixo:

1. **Navegue até o diretório do projeto e clone o repositório:**:
     ```bash
     git clone https://github.com/seu-usuario/user-api-demo.git
     cd user-api-demo

2. **Execute o projeto com Gradle:**:
    ```bash
     ./gradlew run

3. **A API estará disponível em:**:
     ```bash
     http://localhost:8080

Para testar a API, você pode usar o **Postman**, **Insomnia** ou comandos **curl**.

### Endpoints Disponíveis

| **Método** | **Endpoint**       | **Descrição**                    | **Exemplo de Entrada**                                                                 |
|------------|--------------------|----------------------------------|--------------------------------------------------------------------------------------|
| **POST**   | `/user/createUser`  | Cria um novo usuário.            | `{ "name": "João", "email": "joao@email.com", "password": "123456" }`                 |
| **GET**    | `/user/getUser/{email}`      | Busca detalhes de um usuário.    | `email = teste@email.com`                                                                             |
| **PUT**    | `/user/edit`  | Editar um usuário.            | `{ "name": "Leonel", "email": "joao@email.com", "password": "20567" }`                 |
| **DELETE**    | `/user/delete`  | Editar um usuário.            | `{  "email": "joao@email.com" }`                 |
| **GET**    | `/user/alluser`  | Trazer todos os Usuarios          |                  |

---

### Exemplos com curl

**Cadastro de Usuário:**
```bash
curl -X POST http://localhost:8080/user/createUser \
-d '{"name": "João", "email": "joao@email.com", "password": "123456"}'
-message 'O usuário João, foi cadastrado com sucesso com o ID: 4'
```
**Procurar por Usuário:**
```bash
curl -X POST http://127.0.0.1:8080/user/getUser?email=joao@email.com \
-message 'User(id=3, name=João, email=joao@email.com, password=123456)'
```

## Contactos

Se você tiver alguma dúvida, feedback ou preocupação sobre a API, sinta-se à vontade para entrar em contato comigo:

- **Email**: [leonel.francisco@my.istec.pt](mailto:leonel.francisco@my.istec.pt)
- **Phone**: [+351929393928](tel:+351929393928)
- **Redes Sociais**:
  - [Twitter](https://x.com/lionelmendes_)

Estou sempre aberto para ouvir suas opiniões e sugestões! Quer seja um relatório de bug, uma solicitação de recurso ou apenas para dizer olá, não hesite em entrar em contato. Seu feedback é altamente valorizado e ajuda a melhorar a API para todos.
