package com.example.services

import com.example.model.User

interface IUserServices {
    suspend fun allUsers(): List<User> // Retorna todos os usuários cadastrados.
    suspend fun getUser(email: String): User? // Busca um usuário pelo e-mail.
    suspend fun createUser(name: String, email: String, password: String): String? // Cria um novo usuário.
    suspend fun updateUser(name: String, email: String, password: String): String? // Atualiza dados de um usuário.
    suspend fun deleteUser(email: String): String? // Deleta um usuário pelo e-mail.
}