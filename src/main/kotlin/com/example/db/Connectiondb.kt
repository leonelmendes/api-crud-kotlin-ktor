package com.example.db

import com.example.model.Users
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

// Define um objeto para gerenciar a conexão com o banco de dados PostgreSQL e executar transações assíncronas.
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

    // Função suspensa para executar uma transação no banco de dados de forma assíncrona, utilizando Dispatchers.IO para operações de I/O.
    suspend fun <T> dbQuery(block: () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}