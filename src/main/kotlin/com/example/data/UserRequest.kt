package com.example.data

import kotlinx.serialization.Serializable

@Serializable
data class UserRequest(
    val name: String, // propriedade que armazena o nome
    val email: String,  // propriedade que armazena o email
    val password: String    // propriedade que armazena a password
)
