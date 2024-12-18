package com.example.services

import com.example.db.Connectiondb.dbQuery
import com.example.model.User
import com.example.model.Users
import com.example.model.toUser
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.statements.InsertStatement
import java.sql.SQLException

class UserServices : IUserServices {
    // Função para recuperar todos os usuários do banco de dados
    override suspend fun allUsers(): List<User> = dbQuery{
        // Executa uma query no banco de dados para selecionar todos os registros da tabela "Users"
        Users.selectAll()
            // Mapeia cada linha de resultado (ResultRow) para o objeto de domínio "User" usando a função de transformação "resultRowToUser"
            .map(::resultRowToUser)
    }

    // Função para recuperar um usuário específico do banco de dados, usando o email como critério de busca
    override suspend fun getUser(email: String): User? = dbQuery {
        // Realiza uma consulta na tabela "Users" filtrando pelo campo "email" igual ao email fornecido
        Users.select{
            Users.email eq email } // Condição: email na tabela deve ser igual ao email passado como parâmetro
            // Mapeia o resultado da consulta (ResultRow) para o objeto de domínio "User" usando a função "resultRowToUser"
            .map(::resultRowToUser)
            // Obtém o único resultado ou retorna null se nenhum usuário for encontrado
            .singleOrNull()
    }

    // Função para criar um novo usuário no banco de dados
    override suspend fun createUser(name: String, email: String, password: String): String? {
        // Variável para armazenar e retornar a mensagem de resposta
        var response: String? = null
        try {
            //chama o metodo emailexists e valida se o email já existe
            if(emailExists(email))
            {
                // Executa a inserção do usuário na tabela "Users" e retorna o ID gerado
                val id = dbQuery {
                     Users.insert {
                         it[Users.name] = name // Atribui o nome ao campo "name"
                         it[Users.email] = email // Atribui o e-mail ao campo "email"
                         it[Users.password] = password // Atribui a senha ao campo "password"
                    } get Users.id // Recupera o ID do usuário recém-criado
                }
                // Define a mensagem de sucesso incluindo o ID do usuário criado
                response = "O usuário $name, foi cadastrado com sucesso com o ID: ${id}"
            }
            else{
                // Mensagem de erro caso o e-mail já esteja cadastrado
                response = "O e-mail $email já está cadastrado."
            }
        }
        catch (e: Exception) {
            // Lida com possíveis exceções
            e.printStackTrace() // Exibe o stack trace no console para depuração
            response = "Ocorreu um erro ao cadastrar o usuário: ${e.message}"
        }
        // Retorna a mensagem de resposta
        return response
    }

    // Função para atualizar as informações de um usuário no banco de dados
    override suspend fun updateUser(name: String, email: String, password: String): String? {
        // Variável para armazenar e retornar a mensagem de resposta
        var response: String? = null
        try {
            // Realiza a atualização no banco de dados e verifica se algum registro foi afetado
            val updateResult = dbQuery {
                Users.update({ Users.email eq email }) { // Condição: atualiza o usuário cujo e-mail corresponde
                    it[Users.name] = name // Atualiza o campo "name"
                    it[Users.password] = password // Atualiza o campo "password"
                } > 0 // Verifica se o número de linhas afetadas é maior que zero
            }

            // Verifica se a atualização foi bem-sucedida
            if (updateResult) {
                response = "Dados atualizados com sucesso. Nome: ${name}, conta pertecente ao E-mail: ${email}"
            }
            else{
                response = "Falha ao atualizar os dados. Verifique as informações fornecidas."
            }
        }
        catch (e: Exception)
        {
            // Lida com possíveis exceções
            e.printStackTrace()// Log para análise de erros
            response = "Erro ao atualizar os dados no banco de dados. Por favor, tente novamente mais tarde : ${e.message}"
        }
        // Retorna a mensagem de resposta
        return response
    }

    // Função para deletar um usuário no banco de dados
    override suspend fun deleteUser(email: String): String? {
        // Variável para armazenar e retornar a mensagem de resposta
        var response: String? = null
        try {
            // Realiza a exclusão do usuário cujo e-mail corresponde e verifica se foi bem-sucedida
            val deletedResult = dbQuery {
                Users.deleteWhere { Users.email eq email } > 0 // Verifica se linhas foram afetadas
            }

            // Verifica se a exclusão foi bem-sucedida
            if (deletedResult) {
                response = "Usuário deletado com sucesso."
            }
            else{
                response = "Ops! Algo deu errado."
            }
        }
        catch (e: Exception) {
            // Lida com possíveis exceções
            e.printStackTrace()// Log para análise de erros
            response = "Erro ao atualizar os dados no banco de dados. Por favor, tente novamente mais tarde : ${e.message}"
        }
        // Retorna a mensagem de resposta
        return response
    }

    // Função para converter uma linha de resultado do banco em um objeto User
    private fun resultRowToUser(row : ResultRow) = User(
        id = row[Users.id], // Obtém o ID do usuário
        name = row[Users.name].toString(), // Obtém o nome do usuário
        email = row[Users.email].toString(), // Obtém o e-mail do usuário
        password = row[Users.password].toString() // Obtém a senha do usuário
    )

    // Função para verificar se um e-mail já está cadastrado no banco de dados
    private suspend fun emailExists(email: String) : Boolean{

        // Variável que armazena o resultado da consulta ao banco de dados
        val response = dbQuery {
            // Realiza uma seleção na tabela Users filtrando pelo campo 'email' correspondente
            Users.select( Users.email eq email )
                .count() > 0 // Conta o número de registros encontrados e verifica se é maior que 0
        }
        // Verifica o resultado da consulta
        if(response)
        {
            // Se houver um registro correspondente, significa que o e-mail já existe
            return false
        }
        else{
            // Caso contrário, o e-mail não está cadastrado
            return true
        }
        // endregion
    }
}