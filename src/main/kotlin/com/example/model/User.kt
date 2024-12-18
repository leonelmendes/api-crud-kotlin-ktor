package com.example.model

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table

@Serializable // Anotação que indica que essa classe pode ser serializada/deserializada (usada para JSON, por exemplo)
data class User(
    val id: Int,
    val name: String,
    val email: String,
    val password: String
)

// Representação da tabela Users no banco de dados
object Users : Table() {
    // Definição das colunas da tabela
    val id = integer("id").autoIncrement() // Coluna ID que será incrementada automaticamente
    val name = varchar("name", 255).nullable() // Coluna nome, aceita valores nulos
    val email = varchar("email", 255).nullable().uniqueIndex("users_email_unique")
    // Coluna e-mail, aceita valores nulos e possui um índice único para evitar duplicidade
    val password = varchar("password", 255).nullable() // Coluna senha, aceita valores nulos

    override val primaryKey = PrimaryKey(id) // Define o ID como chave primária da tabela
}

// Função de extensão para converter uma linha do banco de dados em um objeto User
fun ResultRow?.toUser(): User? {
    // Verifica se a linha é nula; se for, retorna null
    return if (this == null) null
    else User(
        id = this[Users.id], // Extrai o ID da linha
        email = this[Users.email].toString(), // Extrai o e-mail e converte para string
        name = this[Users.name].toString(), // Extrai o nome e converte para string
        password = this[Users.password].toString() // Extrai a senha e converte para string
    )
}
