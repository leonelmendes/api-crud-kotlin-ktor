package com.example

import com.example.data.UserRequest
import com.example.model.User
import com.example.services.IUserServices
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*

fun Application.configureRouting(services: IUserServices) {
    routing {
        route("/user"){ // Define a rota principal "/user" para agrupar as operações de usuário.

            post("/createUser")
            {
                try {
                    // Recebe o corpo da requisição e deserializa o JSON para a classe UserRequest.
                    val request = call.receive<UserRequest>()

                    // Valida se os campos obrigatórios (nome, email, senha) foram preenchidos.
                    if(request.name.isEmpty() || request.email.isEmpty() || request.password.isEmpty())
                    {
                        // Lança uma exceção caso algum campo esteja vazio.
                        throw IllegalArgumentException("Todos os campos (nome, email, senha) são obrigatórios.")
                    }

                    // Chama o mét*odo de serviço para criar o usuário, passando os dados recebidos.
                    val responseMessage = services.createUser(request.name, request.email, request.password)
                        ?: throw IllegalStateException("Falha ao criar usuário. Por favor, tente novamente mais tarde.")

                    // Responde com sucesso
                    call.respond(
                        status = HttpStatusCode.OK,
                        message = responseMessage)
                } catch (e: Exception) {
                    // Lida com exceções e responde com erro
                    call.respond(
                        status = HttpStatusCode.BadRequest,
                        message = "Ops, algo deu errado: " to "${e.message}"
                    )
                }
            }

            get("/allusers") {
                // Recebe uma lista de dados
                val listOfUsers = services.allUsers()

                // Valida se a lista esta vazia e retorna empty caso esteja vazia,
                if(listOfUsers.isEmpty()){
                    call.respond("empty")
                }else{
                    // Aqui responde com a lista dos dados encontrados
                    call.respond(listOfUsers.toString())
                }
            }

            get("getUser") {
                try{
                    // Cria uma variável para receber o valor do parâmetro 'email' da URL
                    val email = call.parameters["email"]

                    // Verifica se o email está vazio ou nulo, e retorna uma mensagem de erro se necessário
                    if (email.isNullOrBlank()) {
                        call.respond("e-mail é obrigatório.")
                    }
                    else{
                        // Se o email estiver preenchido, chama o serviço para buscar o usuário
                        var request = services.getUser(email.toString())

                        // Verifica se o usuário foi encontrado
                        if(request != null){
                            // Se encontrado, retorna os dados do usuário (conversão para string)
                            call.respond(
                                status = HttpStatusCode.OK,
                                message = request.toString())
                        }
                        // Se não encontrar, responde com uma mensagem indicando que não foi encontrado
                        call.respond(
                            status = HttpStatusCode.NotFound,
                            message = "Não foi encontrado nenhum dado com o email fornecido")

                    }
                }
                catch(e: Exception){
                    // Caso ocorra uma exceção, responde com uma mensagem genérica de erro
                    call.respond("Ops! Algo deu errado, tente novamente mais tarde.")
                }
            }

            put("/edit") {
                try {
                    // Recebe e deserializa o JSON para a classe UserRequest
                    val request = call.receive<UserRequest>()

                    // Valida os campos obrigatórios para garantir que nome, e-mail e senha foram fornecidos
                    if(request.name.isEmpty() || request.email.isEmpty() || request.password.isEmpty())
                    {
                        // Se algum campo estiver vazio, lança uma exceção com uma mensagem de erro
                        throw IllegalArgumentException("Todos os campos (nome, email, senha) são obrigatórios.")
                    }

                    // Chama o serviço para atualizar os dados do usuário
                    val responseMessage = services.updateUser(request.name, request.email, request.password)
                        ?: throw IllegalStateException("Falha ao atualizar os dados do usuário. Por favor, tente novamente mais tarde.")

                    // Responde com a mensagem de sucesso após atualizar os dados
                    call.respond(
                        mapOf(
                            "message:" to responseMessage, // Envia a resposta com o status da operação
                        )
                    )
                } catch (e: Exception) {
                    // Lida com exceções e responde com erro
                    call.respond(
                        status = HttpStatusCode.BadRequest, // Retorna o status de erro 400 (Bad Request)
                        message = "Ops, algo deu errado: " to "${e.message}" // Detalha o erro ocorrido
                    )
                }
            }

            delete("/delete")
            {
                try {
                    // Obtém o parâmetro 'email' enviado no corpo da requisição como um Map de Strings
                    val request = call.receive<Map<String, String>>()
                    val email = request["email"]

                    // Verifica se o e-mail foi fornecido, se não, lança uma exceção
                    if(email?.isEmpty() == true) throw IllegalArgumentException("E-mail não fornecido.")

                    // Chama o serviço para deletar o usuário, passando o e-mail como parâmetro
                    val result = services.deleteUser(email.toString())

                    // Verifica se a resposta do serviço contém a palavra "sucesso", ignorando maiúsculas/minúsculas
                    if (result?.contains("sucesso", ignoreCase = true) == true) {
                        // Responde com sucesso se o usuário foi deletado
                        call.respond(
                            // Se o usuário foi deletado com sucesso, responde com uma mensagem de sucesso
                            mapOf(
                                "message" to "Usuário com o e-mail ${email} foi deletado com sucesso."
                            )
                        )
                    } else {
                        // Se o usuário não foi encontrado, responde com erro 404 (Não Encontrado)
                        call.respond(
                            status = HttpStatusCode.NotFound,
                            message = mapOf("error" to "Usuário com o e-mail ${email} não encontrado.")
                        )
                    }
                } catch (e: Exception) {
                    // Lida com exceções e responde com erro 400 (Bad Request) se algo der errado
                    call.respond(
                        status = HttpStatusCode.BadRequest,
                        message = mapOf("error" to (e.message ?: "Erro inesperado ao deletar usuário."))
                    )
                }
            }
        }
    }
}
