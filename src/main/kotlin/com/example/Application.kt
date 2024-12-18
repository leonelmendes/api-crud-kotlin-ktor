package com.example

import com.example.db.Connectiondb
import com.example.services.IUserServices
import com.example.services.UserServices
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    val dao : IUserServices = UserServices()
    Connectiondb.init()

    configureSerialization()
    configureRouting(dao)
}
